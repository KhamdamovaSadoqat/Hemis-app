package com.software.hemis.presenter.subject.grade.attendance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hemis.databinding.ItemAttendanceBinding
import com.software.hemis.utils.AutoUpdatableAdapter
import kotlin.properties.Delegates

class AttendanceInnerAdapter:
    RecyclerView.Adapter<AttendanceInnerAdapter.VH>(), AutoUpdatableAdapter {

    var items: List<String> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            ItemAttendanceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind()
    }

    override fun getItemCount() : Int = 2

    class VH(binding: ItemAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {
        }
    }
}