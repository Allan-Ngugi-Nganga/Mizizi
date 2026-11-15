package com.example.mizizi.ui.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mizizi.R
import com.example.mizizi.databinding.FragmentPracticeBinding
import com.example.mizizi.domain.Grade
import com.example.mizizi.ui.viewmodel.HintState
import com.example.mizizi.ui.viewmodel.MiziziViewModel

/**
 * The practice session: one proverb at a time, an answer box, and feedback
 * based on meaning instead of exact wording.
 */
class PracticeFragment : Fragment() {

    private var _binding: FragmentPracticeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MiziziViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPracticeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewModel.session.value?.isActive.orFalse()) {
            viewModel.startSession()
        }

        binding.submitButton.setOnClickListener {
            viewModel.submitAnswer(binding.answerEditText.text?.toString().orEmpty())
        }

        binding.nextButton.setOnClickListener {
            binding.answerEditText.text?.clear()
            viewModel.nextQuestion()
        }

        binding.hintButton.setOnClickListener { viewModel.requestHint() }

        binding.fullFeedbackButton.setOnClickListener {
            val outcome = viewModel.outcome.value ?: return@setOnClickListener
            val action = PracticeFragmentDirections.actionPracticeFragmentToResultFragment(
                proverbOriginal = outcome.proverb.original,
                meaning = outcome.proverb.meaning,
                answer = outcome.answer,
                grade = outcome.grade.name,
                matchedKeywords = outcome.matchedKeywords.joinToString(", "),
                sessionFinished = false
            )
            findNavController().navigate(action)
        }

        binding.restartButton.setOnClickListener {
            binding.answerEditText.text?.clear()
            viewModel.startSession()
        }

        observeSession()
        observeOutcome()
        observeHint()
    }

    private fun observeSession() {
        viewModel.session.observe(viewLifecycleOwner) { state ->
            val proverb = state.current

            binding.counterText.text = if (state.totalQuestions > 0) {
                getString(R.string.practice_counter, state.questionNumber, state.totalQuestions)
            } else {
                getString(R.string.practice_no_proverbs)
            }
            binding.scoreText.text = getString(R.string.practice_score, state.score)
            binding.proverbText.text = proverb?.original.orEmpty()
            binding.proverbText.visibility = if (proverb == null) View.GONE else View.VISIBLE

            val finished = state.finished
            binding.summaryText.visibility = if (finished) View.VISIBLE else View.GONE
            binding.restartButton.visibility = if (finished) View.VISIBLE else View.GONE
            binding.submitButton.isEnabled = !finished
            binding.hintButton.isEnabled = !finished
            binding.answerInputLayout.visibility = if (finished) View.GONE else View.VISIBLE

            if (finished) {
                binding.summaryText.text = getString(
                    R.string.practice_summary,
                    state.score,
                    state.totalQuestions
                )
                binding.outcomeCard.visibility = View.GONE
            }
        }
    }

    private fun observeOutcome() {
        viewModel.outcome.observe(viewLifecycleOwner) { outcome ->
            if (outcome == null) {
                binding.outcomeCard.visibility = View.GONE
                return@observe
            }

            binding.outcomeCard.visibility = View.VISIBLE
            binding.outcomeGradeText.text = outcome.grade.label()
            binding.outcomeGradeText.setTextColor(
                requireContext().getColor(
                    when (outcome.grade) {
                        Grade.CORRECT -> R.color.green_primary
                        Grade.CLOSE -> R.color.ink
                        Grade.INCORRECT -> R.color.ink_muted
                    }
                )
            )
            binding.outcomeMeaningText.text = outcome.proverb.meaning
            binding.outcomeKeywordsText.text = if (outcome.matchedKeywords.isEmpty()) {
                getString(R.string.practice_no_keywords)
            } else {
                getString(R.string.practice_matched_keywords, outcome.matchedKeywords.joinToString(", "))
            }
        }
    }

    private fun observeHint() {
        viewModel.hint.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HintState.Idle -> {
                    binding.hintText.visibility = View.GONE
                    binding.hintButton.isEnabled = true
                }
                is HintState.Loading -> {
                    binding.hintText.visibility = View.VISIBLE
                    binding.hintText.text = getString(R.string.practice_hint_loading)
                    binding.hintButton.isEnabled = false
                }
                is HintState.Loaded -> {
                    binding.hintText.visibility = View.VISIBLE
                    binding.hintText.text = getString(R.string.practice_hint_result, state.translation)
                    binding.hintButton.isEnabled = true
                }
                is HintState.Failed -> {
                    binding.hintText.visibility = View.VISIBLE
                    binding.hintText.text = state.message
                    binding.hintButton.isEnabled = true
                }
            }
        }
    }

    private fun Boolean?.orFalse(): Boolean = this == true

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
