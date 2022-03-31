package com.software.hemis.presenter.schedule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hemis.R
import com.example.hemis.databinding.ItemTableBinding
import com.software.hemis.domain.main.schedule.ScheduleEntity
import com.software.hemis.utils.AutoUpdatableAdapter
import com.software.hemis.utils.checkingTime
import kotlin.properties.Delegates

class ScheduleAdapter(private val itemClickListener: (Int) -> Unit) :
    RecyclerView.Adapter<ScheduleAdapter.VH>(), AutoUpdatableAdapter {

    var items: List<ScheduleEntity> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            ItemTableBinding.inflate(
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

    class VH(private val binding: ItemTableBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ScheduleEntity) {
            binding.apply {
                tvSubjectName.text = data.subjectName
                tvTeacher.text = data.employeeName
                tvRoom.text = data.auditoriumName
                tvSubjectType.text = data.trainingName
                val checkTime = checkingTime(data.lessonPairStartTime, data.lessonDate)
                tvBeginTime.text = checkTime.startTime
                tvEndTime.text = checkTime.endTime
                when (checkTime.state) {
                    //state: 1-->passed
                    //       2-->currently running
                    //       3-->upcoming
                    1 -> {
                        llTime.setBackgroundColor(ContextCompat.getColor(context, R.color.card_time))
                        tvRoom.setBackgroundColor(ContextCompat.getColor(context, R.color.card_time))
                    }
                    2 -> {
                        llTime.background = ContextCompat.getDrawable(context, R.drawable.bg_gradient_green)
                        tvRoom.background = ContextCompat.getDrawable(context, R.drawable.bg_gradient_green)
                    }
                    3 -> {
                        llTime.background = ContextCompat.getDrawable(context, R.drawable.bg_gradient_orange)
                        tvRoom.background = ContextCompat.getDrawable(context, R.drawable.bg_gradient_orange)
                    }
                }
            }
        }
    }
}

data class CheckTime(
    val startTime: String,
    val endTime: String,
    val state: Int
)