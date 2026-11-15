package com.example.mizizi.ui.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mizizi.R
import com.example.mizizi.data.model.ProverbEntity
import com.example.mizizi.databinding.FragmentProverbEditorBinding
import com.example.mizizi.ui.viewmodel.FormResult
import com.example.mizizi.ui.viewmodel.MiziziViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Create, update or delete a proverb. One screen covers both the create and the
 * update half of CRUD: opening it with a proverb id pre-fills the fields and
 * shows the delete action.
 */
class ProverbEditorFragment : Fragment() {

    private var _binding: FragmentProverbEditorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MiziziViewModel by activityViewModels()

    private var editing: ProverbEntity? = null
    private var loaded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProverbEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val proverbId = ProverbEditorFragmentArgs.fromBundle(requireArguments()).proverbId

        if (proverbId > 0) {
            binding.editorTitleText.text = getString(R.string.editor_title_edit)
            binding.deleteButton.visibility = View.VISIBLE
            viewModel.proverbs.observe(viewLifecycleOwner) { proverbs ->
                if (loaded) return@observe
                proverbs.firstOrNull { it.id == proverbId }?.let { proverb ->
                    loaded = true
                    editing = proverb
                    binding.originalEditText.setText(proverb.original)
                    binding.meaningEditText.setText(proverb.meaning)
                    binding.keywordsEditText.setText(proverb.keywordList().joinToString(", "))
                }
            }
        } else {
            binding.editorTitleText.text = getString(R.string.editor_title_add)
        }

        binding.saveButton.setOnClickListener { save() }
        binding.deleteButton.setOnClickListener { confirmDelete() }

        viewModel.formResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is FormResult.Invalid -> showError(result.message)
                is FormResult.Saved -> {
                    viewModel.clearFormResult()
                    Toast.makeText(
                        requireContext(),
                        R.string.editor_saved,
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                }
                null -> binding.formErrorText.visibility = View.GONE
            }
        }
    }

    private fun save() {
        val original = binding.originalEditText.text?.toString().orEmpty()
        val meaning = binding.meaningEditText.text?.toString().orEmpty()
        val keywords = binding.keywordsEditText.text?.toString().orEmpty()

        val current = editing
        if (current == null) {
            viewModel.createProverb(original, meaning, keywords)
        } else {
            viewModel.editProverb(current, original, meaning, keywords)
        }
    }

    private fun confirmDelete() {
        val current = editing ?: return
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.editor_delete_title)
            .setMessage(getString(R.string.editor_delete_message, current.original))
            .setNegativeButton(R.string.archive_cancel, null)
            .setPositiveButton(R.string.archive_delete) { _, _ ->
                viewModel.deleteProverb(current)
                findNavController().popBackStack()
            }
            .show()
    }

    private fun showError(message: String) {
        binding.formErrorText.text = message
        binding.formErrorText.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
