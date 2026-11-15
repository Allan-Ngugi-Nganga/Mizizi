package com.example.mizizi.ui.daily

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mizizi.R
import com.example.mizizi.data.model.ProverbEntity
import com.example.mizizi.databinding.FragmentDailyProverbBinding
import com.example.mizizi.ui.viewmodel.MiziziViewModel
import com.example.mizizi.work.DailyProverbWorker
import com.example.mizizi.work.Notifications
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * The proverb of the day, its machine translation, and the switch that puts the
 * daily reminder into WorkManager.
 */
class DailyProverbFragment : Fragment() {

    private var _binding: FragmentDailyProverbBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MiziziViewModel by activityViewModels()

    private var extras = 0

    private val notificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) scheduleReminder()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyProverbBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Notifications.ensureChannel(requireContext())

        viewModel.proverbs.observe(viewLifecycleOwner) { proverbs ->
            show(proverbs)
        }

        binding.anotherButton.setOnClickListener {
            extras++
            show(viewModel.proverbs.value.orEmpty())
        }

        binding.reminderButton.setOnClickListener { enableReminder() }

        updateReminderStatus()
    }

    private fun show(proverbs: List<ProverbEntity>) {
        if (proverbs.isEmpty()) {
            binding.proverbText.text = getString(R.string.practice_no_proverbs)
            binding.meaningText.text = ""
            binding.machineText.text = ""
            return
        }

        val dayIndex = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + extras
        val index = ((dayIndex % proverbs.size) + proverbs.size) % proverbs.size
        val proverb = proverbs[index]

        binding.proverbText.text = proverb.original
        binding.meaningText.text = proverb.meaning

        binding.machineProgress.visibility = View.VISIBLE
        binding.machineText.text = getString(R.string.practice_hint_loading)
        viewModel.requestMachineTranslation(proverb.original) { text ->
            if (_binding == null) return@requestMachineTranslation
            binding.machineProgress.visibility = View.GONE
            binding.machineText.text = getString(R.string.practice_hint_result, text)
        }
    }

    private fun enableReminder() {
        val needsPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED

        if (needsPermission) {
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            scheduleReminder()
        }
    }

    private fun scheduleReminder() {
        val request = PeriodicWorkRequestBuilder<DailyProverbWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(1, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )

        updateReminderStatus()
    }

    private fun updateReminderStatus() {
        WorkManager.getInstance(requireContext())
            .getWorkInfosForUniqueWorkLiveData(WORK_NAME)
            .observe(viewLifecycleOwner) { infos ->
                val scheduled = infos.any { !it.state.isFinished }
                binding.reminderStatusText.text = getString(
                    if (scheduled) R.string.daily_reminder_on else R.string.daily_reminder_off
                )
                binding.reminderButton.isEnabled = !scheduled
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        const val WORK_NAME = "mizizi_daily_proverb"
    }
}
