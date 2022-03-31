package com.software.hemis.presenter.subject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hemis.R
import com.example.hemis.databinding.ItemSubjectBinding
import com.software.hemis.domain.main.subject.SubjectWithSubjectDetails
import com.software.hemis.utils.AutoUpdatableAdapter
import kotlin.properties.Delegates

class SubjectAdapter(private val itemClickListener: (Int) -> Unit) :
    RecyclerView.Adapter<SubjectAdapter.VH>(), AutoUpdatableAdapter {

    var items: List<SubjectWithSubjectDetails> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            ItemSubjectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.invoke(items[position].subject.subjectId)
        }
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class VH(private val binding: ItemSubjectBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(subjectEntity: SubjectWithSubjectDetails) {
            binding.tvSubjectName.text = subjectEntity.subject.subjectName
            val str = binding.root.context.getString(
                R.string.subject_details,
                subjectEntity.subject.subjectTypeName,
                subjectEntity.subject.totalAcload.toString(),
                subjectEntity.subject.credit.toString()
            )
            binding.tvSubjectDetails.text = str
            binding.tvSubjectScore.text = binding.root.context.getString(
                R.string.task_count,
                subjectEntity.subjectDetails.submitsCount.toString(),
                subjectEntity.subjectDetails.tasksCount.toString()
            )
            if(subjectEntity.subjectDetails.tasksCount == 0) binding.animatedProgressBar.setProgress(0)
            else binding.animatedProgressBar.setProgress(subjectEntity.subjectDetails.submitsCount * 100 / subjectEntity.subjectDetails.tasksCount)
        }
    }

}