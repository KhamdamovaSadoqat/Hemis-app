package com.software.hemis.presenter.deadline.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hemis.R
import com.example.hemis.databinding.ItemTaskBinding
import com.software.hemis.domain.main.task.TaskWithSubject
import com.software.hemis.utils.AutoUpdatableAdapter
import com.software.hemis.utils.data.Constants
import com.software.hemis.utils.deadlineType
import com.software.hemis.utils.secondsToDateAndTime
import kotlin.properties.Delegates

class TaskRecyclerAdapter(private val itemClickListener: (TaskWithSubject) -> Unit) :
    RecyclerView.Adapter<TaskRecyclerAdapter.VH>(),
    AutoUpdatableAdapter {

    var items: List<TaskWithSubject> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            parent.context)

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(items[position])
        holder.itemView.setOnClickListener {
            itemClickListener.invoke(items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    class VH(private val binding: ItemTaskBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(task: TaskWithSubject) {
            binding.tvTaskDeadline.isSelected = true
            binding.apply {
                tvTaskName.text = task.taskEntity.name
                tvSubjectName.text = task.subject.subjectName
                tvTaskField.text = task.taskEntity.trainingType
                tvTaskDeadline.text = secondsToDateAndTime(task.taskEntity.deadline)
                when (task.taskEntity.taskStatusCode) {
                    Constants.MARKED -> marked()
                    Constants.UPLOADED -> uploaded()
                    Constants.GIVEN ->
                        when (deadlineType(task.taskEntity.deadline)) {
                            Constants.WAITING_UPLOAD -> waitingUpload()
                            Constants.LESS_THAN_TWO_DAYS -> lessThanWeek(context.getString(R.string.less_than_week, 2))
                            Constants.LESS_THAN_THREE_DAYS -> lessThanWeek(context.getString(R.string.less_than_week, 3))
                            Constants.LESS_THAN_FOUR_DAYS -> lessThanWeek(context.getString(R.string.less_than_week, 4))
                            Constants.LESS_THAN_FIVE_DAYS -> lessThanWeek(context.getString(R.string.less_than_week, 5))
                            Constants.LESS_THAN_SIX_DAYS -> lessThanWeek(context.getString(R.string.less_than_week, 6))
                            Constants.LESS_THAN_SEVEN_DAYS -> lessThanWeek(context.getString(R.string.less_than_week, 7))
                            Constants.NEW_TASK -> newTask()
                            Constants.DEADLINE_PASSED -> deadlinePassed()
                        }
                }
            }
        }

        private fun marked() {
            binding.tvCardStatus.text = context.getString(R.string.task_marked)
            binding.tvCardStatus.setTextColor(ContextCompat.getColor(context, R.color.purple))
            binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(context,
                R.drawable.img_marked))
            binding.cardStatus.background =
                ContextCompat.getDrawable(context, R.drawable.bg_task_marked)
        }

        private fun uploaded() {
            binding.tvCardStatus.text = context.getString(R.string.task_uploaded)
            binding.tvCardStatus.setTextColor(ContextCompat.getColor(context, R.color.green))
            binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_done))
            binding.cardStatus.background =
                ContextCompat.getDrawable(context, R.drawable.bg_task_uploaded)
        }

        private fun waitingUpload() {
            binding.tvCardStatus.text = context.getString(R.string.task_waiting)
            binding.tvCardStatus.setTextColor(ContextCompat.getColor(context, R.color.white))
            binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_fire))
            binding.cardStatus.background =
                ContextCompat.getDrawable(context, R.drawable.bg_task_waiting)
        }

        private fun lessThanWeek(textStatus: String) {
            binding.tvCardStatus.text = textStatus
            binding.tvCardStatus.setTextColor(ContextCompat.getColor(context, R.color.yellow))
            binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(context,
                R.drawable.img_warning))
            binding.cardStatus.background =
                ContextCompat.getDrawable(context,
                    R.drawable.bg_task_less_than_three_days)
        }

        private fun deadlinePassed() {
            binding.tvCardStatus.text = context.getString(R.string.deadline_passed)
            binding.tvCardStatus.setTextColor(ContextCompat.getColor(context, R.color.red))
            binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(context,
                R.drawable.img_deadline))
            binding.cardStatus.background =
                ContextCompat.getDrawable(context,
                    R.drawable.bg_task_deadine_passed)
        }

        private fun newTask() {
            binding.tvCardStatus.text = context.getString(R.string.new_task)
            binding.tvCardStatus.setTextColor(ContextCompat.getColor(context, R.color.light_blue_darker))
            binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(context,
                R.drawable.img_new))
            binding.cardStatus.background =
                ContextCompat.getDrawable(context,
                    R.drawable.bg_task_new)
        }
    }
}