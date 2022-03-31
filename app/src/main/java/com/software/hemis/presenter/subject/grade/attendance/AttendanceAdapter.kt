package com.software.hemis.presenter.subject.grade.attendance

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hemis.R
import com.example.hemis.databinding.ItemAttendanceDateBinding
import com.software.hemis.domain.main.attendance.AttendanceEntity
import com.software.hemis.utils.AutoUpdatableAdapter
import com.software.hemis.utils.secondsToDate
import com.software.hemis.utils.secondsToDateAndTime
import kotlin.properties.Delegates

class AttendanceAdapter :
    RecyclerView.Adapter<AttendanceAdapter.VH>(), AutoUpdatableAdapter {

    private var lastAttendanceDate: String? = null

    var items: List<AttendanceEntity> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            ItemAttendanceDateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        if(secondsToDate(items[position].lessonDate) == lastAttendanceDate)
            holder.onBind(items[position], true)
        else {
            holder.onBind(items[position], false)
            lastAttendanceDate = secondsToDate(items[position].lessonDate)
        }
    }

    override fun getItemCount(): Int = items.size

    class VH(private val binding: ItemAttendanceDateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(attendance: AttendanceEntity, haveLastAttandanceDate: Boolean) {
            binding.apply {
                if(haveLastAttandanceDate)
                    tvDate.visibility = TextView.GONE
                else
                    tvDate.text = secondsToDate(attendance.lessonDate)

                tvSubjectName.text = attendance.subjectName
//                tvSubjectLecture.text = attendance.
                tvSubjectType.text = attendance.trainingTypeName
                tvDateTime.text = secondsToDateAndTime(attendance.lessonDate)
                if (attendance.absentOff != 0) tvAbsentHours.text =
                    binding.root.context.getString(R.string.absent_hours, attendance.absentOff)
                if (attendance.absentOn != 0) tvAbsentHours.text =
                    binding.root.context.getString(R.string.absent_hours, attendance.absentOn)

            }
        }
    }
}