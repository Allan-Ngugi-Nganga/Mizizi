package com.example.mizizi.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mizizi.R
import com.example.mizizi.databinding.FragmentHomeBinding
import com.example.mizizi.ui.viewmodel.MiziziViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * The landing screen: how much material the user has, and the way in to
 * practice, the archive and the daily proverb.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MiziziViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.proverbs.observe(viewLifecycleOwner) { proverbs ->
            binding.proverbCountText.text =
                resources.getQuantityString(R.plurals.home_proverb_count, proverbs.size, proverbs.size)
            binding.emptyStateText.visibility =
                if (proverbs.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.attempts.observe(viewLifecycleOwner) { attempts ->
            binding.attemptCountText.text =
                resources.getQuantityString(R.plurals.home_attempt_count, attempts.size, attempts.size)
        }

        binding.startPracticeButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_practiceFragment)
        }
        binding.archiveButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_archiveFragment)
        }
        binding.addProverbButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_proverbEditorFragment)
        }
        binding.dailyProverbButton.setOnClickListener { notYet("The daily proverb") }
    }

    /** Placeholder until each screen lands in its own slice. */
    private fun notYet(screen: String) {
        Snackbar.make(binding.root, getString(R.string.screen_coming_soon, screen), Snackbar.LENGTH_SHORT)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
