package com.software.hemis.presenter.other.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hemis.databinding.ItemInformationBinding
import com.software.hemis.utils.AutoUpdatableAdapter
import kotlin.properties.Delegates

class InformationRecyclerViewAdapter(private val itemClickListener: (Int) -> Unit) :
    RecyclerView.Adapter<InformationRecyclerViewAdapter.VH>(), AutoUpdatableAdapter {

    var items: List<Info> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            ItemInformationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), parent.context
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.invoke(position)
        }
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class VH(private val binding: ItemInformationBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(info: Info) {
            binding.fab.setImageResource(info.icon)
            binding.tvName.text = (info.name)
            binding.fab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, info.color))
        }
    }
}

data class Info(
    var icon: Int,
    var name: String,
    @ColorRes
    var color: Int
)