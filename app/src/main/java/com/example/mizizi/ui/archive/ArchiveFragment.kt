package com.example.mizizi.ui.archive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mizizi.R
import com.example.mizizi.databinding.FragmentArchiveBinding
import com.example.mizizi.ui.viewmodel.MiziziViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Every attempt the user has recorded, newest first, with a delete per row and
 * a clear-all action. Empty state is shown when there is nothing to list.
 */
class ArchiveFragment : Fragment() {

    private var _binding: FragmentArchiveBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MiziziViewModel by activityViewModels()

    private val adapter = ArchiveAdapter { attempt ->
        confirmDelete(attempt.id, attempt.proverbOriginal)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.archiveRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.archiveRecyclerView.adapter = adapter

        viewModel.attempts.observe(viewLifecycleOwner) { attempts ->
            adapter.submitList(attempts)

            val empty = attempts.isEmpty()
            binding.emptyStateGroup.visibility = if (empty) View.VISIBLE else View.GONE
            binding.archiveRecyclerView.visibility = if (empty) View.GONE else View.VISIBLE
            binding.clearArchiveButton.visibility = if (empty) View.GONE else View.VISIBLE
            binding.archiveCountText.text =
                resources.getQuantityString(R.plurals.home_attempt_count, attempts.size, attempts.size)
        }

        binding.clearArchiveButton.setOnClickListener { confirmClearAll() }
    }

    private fun confirmDelete(attemptId: Long, proverb: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.archive_delete_title)
            .setMessage(getString(R.string.archive_delete_message, proverb))
            .setNegativeButton(R.string.archive_cancel, null)
            .setPositiveButton(R.string.archive_delete) { _, _ ->
                viewModel.attempts.value
                    ?.firstOrNull { it.id == attemptId }
                    ?.let(viewModel::deleteAttempt)
            }
            .show()
    }

    private fun confirmClearAll() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.archive_clear_title)
            .setMessage(R.string.archive_clear_message)
            .setNegativeButton(R.string.archive_cancel, null)
            .setPositiveButton(R.string.archive_clear_all) { _, _ -> viewModel.clearArchive() }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
