package com.software.hemis.presenter.help

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hemis.databinding.ItemHelpBinding
import com.software.hemis.utils.AutoUpdatableAdapter
import kotlin.properties.Delegates

class HelpAdapter: RecyclerView.Adapter<HelpAdapter.VH>(),
    AutoUpdatableAdapter {

    var items: List<String> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    class VH (private val binding: ItemHelpBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(text: String, position: Int) {
           binding.apply {
               tvHelp.text = text
               tvHelpNumber.text = position.toString()
           }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
    VH(ItemHelpBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(items[position], position+1)
    }

    override fun getItemCount(): Int  = items.size
}
