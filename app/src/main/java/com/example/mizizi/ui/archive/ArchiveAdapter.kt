package com.example.mizizi.ui.archive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mizizi.R
import com.example.mizizi.data.model.AttemptEntity
import com.example.mizizi.databinding.ItemAttemptBinding
import java.text.DateFormat
import java.util.Date

/**
 * Renders the archive, newest attempt first. A new list is diffed rather than
 * rebuilt, so deleting one row animates instead of redrawing the screen.
 */
class ArchiveAdapter(
    private val onDelete: (AttemptEntity) -> Unit
) : ListAdapter<AttemptEntity, ArchiveAdapter.AttemptViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttemptViewHolder {
        val binding = ItemAttemptBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AttemptViewHolder(binding, onDelete)
    }

    override fun onBindViewHolder(holder: AttemptViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AttemptViewHolder(
        private val binding: ItemAttemptBinding,
        private val onDelete: (AttemptEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(attempt: AttemptEntity) {
            binding.originalText.text = attempt.proverbOriginal
            binding.answerText.text = binding.root.context.getString(
                R.string.archive_your_answer,
                attempt.userAnswer.ifBlank { binding.root.context.getString(R.string.result_no_answer) }
            )
            binding.gradeText.text = binding.root.context.getString(
                R.string.archive_grade_and_date,
                attempt.grade.lowercase().replaceFirstChar { it.uppercase() },
                DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                    .format(Date(attempt.createdAt))
            )
            binding.deleteButton.setOnClickListener { onDelete(attempt) }
        }
    }

    private companion object {
        val DIFF = object : DiffUtil.ItemCallback<AttemptEntity>() {
            override fun areItemsTheSame(oldItem: AttemptEntity, newItem: AttemptEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: AttemptEntity, newItem: AttemptEntity): Boolean =
                oldItem == newItem
        }
    }
}
