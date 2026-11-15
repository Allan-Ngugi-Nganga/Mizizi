package com.example.mizizi.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mizizi.R
import com.example.mizizi.databinding.FragmentResultBinding
import com.example.mizizi.domain.Grade
import com.example.mizizi.ui.viewmodel.MiziziViewModel

/**
 * Full feedback for the attempt that was just made: the grade, what the user
 * typed, the meaning, which meaning words they hit, and the machine's literal
 * translation for comparison.
 *
 * The grades and texts arrive through Safe Args, so the screen can be opened
 * for any proverb, not only the current question.
 */
class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MiziziViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = ResultFragmentArgs.fromBundle(requireArguments())

        binding.proverbText.text = args.proverbOriginal
        binding.answerText.text = args.answer.ifBlank { getString(R.string.result_no_answer) }
        binding.meaningText.text = args.meaning

        val grade = runCatching { Grade.valueOf(args.grade) }.getOrDefault(Grade.INCORRECT)
        binding.gradeText.text = grade.label()
        binding.gradeText.setTextColor(
            requireContext().getColor(
                when (grade) {
                    Grade.CORRECT -> R.color.green_primary
                    Grade.CLOSE -> R.color.ink
                    Grade.INCORRECT -> R.color.ink_muted
                }
            )
        )

        binding.keywordsText.text = when {
            args.matchedKeywords.isBlank() -> getString(R.string.practice_no_keywords)
            else -> getString(R.string.practice_matched_keywords, args.matchedKeywords)
        }

        binding.continueButton.text = if (args.sessionFinished) {
            getString(R.string.result_back_home)
        } else {
            getString(R.string.result_continue)
        }
        binding.continueButton.setOnClickListener {
            if (args.sessionFinished) {
                findNavController().popBackStack(R.id.homeFragment, false)
            } else {
                findNavController().popBackStack()
            }
        }

        requestMachineTranslation(args.proverbOriginal)
    }

    private fun requestMachineTranslation(original: String) {
        binding.machineProgress.visibility = View.VISIBLE
        binding.machineText.text = getString(R.string.practice_hint_loading)

        viewModel.requestMachineTranslation(original) { result ->
            if (_binding == null) return@requestMachineTranslation
            binding.machineProgress.visibility = View.GONE
            binding.machineText.text = result
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
