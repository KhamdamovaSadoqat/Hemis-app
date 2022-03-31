package com.software.hemis.presenter.schedule.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hemis.R
import com.example.hemis.databinding.ItemCalendarDayBinding
import com.software.hemis.utils.AutoUpdatableAdapter
import com.software.hemis.utils.IsToday
import kotlin.properties.Delegates

class ScheduleDaysAdapter(private val itemClickListener: (Int) -> Unit) :
    RecyclerView.Adapter<ScheduleDaysAdapter.VH>(), AutoUpdatableAdapter {

    var items: List<IsToday> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            ItemCalendarDayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), parent.context
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.invoke(position)
        }
        if (items[position].today)
            holder.onBind(items[position])
        else holder.changeColorToDefault(items[position])
    }

    override fun getItemCount(): Int = items.size

    class VH(private val binding: ItemCalendarDayBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: IsToday) {
            binding.apply {
                tvNum.text = data.day
                if (data.today) {
                    tvNum.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_calendar_today)
                    tvNum.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            }
        }

        fun changeColorToDefault(data: IsToday) {
            binding.apply {
                tvNum.text = data.day
                tvNum.background = null
                tvNum.setTextColor(ContextCompat.getColor(context, R.color.black_dark))
            }
        }
    }
}