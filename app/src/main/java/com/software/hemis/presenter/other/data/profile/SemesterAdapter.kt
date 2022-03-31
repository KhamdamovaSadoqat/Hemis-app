package com.software.hemis.presenter.other.data.profile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hemis.databinding.ItemSemesterBinding
import com.software.hemis.domain.main.semester.SemesterEntity
import com.software.hemis.utils.AutoUpdatableAdapter
import com.software.hemis.utils.data.SharedPref
import javax.inject.Inject
import kotlin.properties.Delegates


class SemesterAdapter(private val itemClickListener: (SemesterEntity) -> Unit) :
    RecyclerView.Adapter<SemesterAdapter.VH>(), AutoUpdatableAdapter {

    var changedSemester: Int? = null

    var items: List<SemesterEntity> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            ItemSemesterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.invoke(items[position])
        }
        changedSemester?.let { holder.onBind(items[position], it) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(semesterCode: Int) {
        changedSemester = semesterCode
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    class VH(private val binding: ItemSemesterBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun onBind(data: SemesterEntity, semesterCode: Int) {
            binding.rbText.text = data.name
            binding.rbText.isChecked = data.code.toInt() == semesterCode
        }
    }
}