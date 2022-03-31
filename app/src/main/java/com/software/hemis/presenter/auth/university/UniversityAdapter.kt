package com.software.hemis.presenter.auth.university

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hemis.databinding.ItemUniverityBinding
import com.software.hemis.domain.auth.university.UniversityEntity
import com.software.hemis.utils.AutoUpdatableAdapter
import kotlin.properties.Delegates

class UniversityAdapter(private val itemClickListener: (UniversityEntity) -> Unit) :
    RecyclerView.Adapter<UniversityAdapter.VH>(),
    AutoUpdatableAdapter {

    var items: List<UniversityEntity> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemUniverityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(items[position])
        holder.itemView.setOnClickListener {
            Log.d("UniversityAdapter", "------------- onBindViewHolder: ${items.size}")
            itemClickListener.invoke(items[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        Log.d("UniversityAdapter", "------------- getItemCount: ${items.size}")
       return items.size
    }
    class VH(private val binding: ItemUniverityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(uni: UniversityEntity) {
            binding.tvUni.text = uni.name
        }
    }

}