package com.software.hemis.presenter.subject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hemis.databinding.ItemTaskBinding
import com.software.hemis.utils.AutoUpdatableAdapter
import kotlin.properties.Delegates

class SubjectTypeRecyclerAdapter : RecyclerView.Adapter<SubjectTypeRecyclerAdapter.VH>(), AutoUpdatableAdapter {

    var items: List<String> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    class VH(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind() {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int = 10

}