package com.software.hemis.presenter.subject.grade.recources

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.hemis.databinding.ItemResourcesBinding
import com.software.hemis.domain.main.resources.ResourceEntity
import com.software.hemis.presenter.deadline.adapter.TaskUploadRecyclerAdapter
import com.software.hemis.utils.AutoUpdatableAdapter
import com.software.hemis.utils.secondsToDate
import com.software.hemis.utils.secondsToDateAndTime
import kotlin.properties.Delegates

class ResourcesAdapter :
    RecyclerView.Adapter<ResourcesAdapter.VH>(), AutoUpdatableAdapter {

    private var lastAttendanceDate: String? = null

    var items: List<ResourceEntity> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            ItemResourcesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        if(secondsToDate(items[position].updateAt) == lastAttendanceDate)
            holder.onBind(items[position], true)
        else {
            holder.onBind(items[position], false)
            lastAttendanceDate = secondsToDate(items[position].updateAt)
        }
    }

    override fun getItemCount(): Int = items.size

    class VH(private val binding: ItemResourcesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var adapter: TaskUploadRecyclerAdapter
        private val fileNameList = arrayListOf<String>()
        private val fileUrlList = arrayListOf<Uri>()

        fun onBind(resource: ResourceEntity, haveLastAttendanceDate: Boolean) {
            adapter = TaskUploadRecyclerAdapter{}
            binding.rvFiles.adapter = adapter

            for (file in resource.file.entries) {
                val (key, value) = resource.file.entries.iterator().next()
                fileNameList.add(value ?: "")
                fileUrlList.add(key!!.toUri())
            }

            adapter.fileName = fileNameList
            adapter.fileUrl = fileUrlList

            binding.apply {
                //tvDate.text = secondsToDate(resource.updateAt)

                if(haveLastAttendanceDate)
                    tvDate.visibility = TextView.GONE
                else
                    tvDate.text = secondsToDate(resource.updateAt)

                tvSubjectName.text = "resource.name"
                tvSubjectDetail.text = resource.comment
                tvSubjectType.text = resource.trainingTypeName
                tvDateTime.text = secondsToDateAndTime(resource.updateAt)
            }
        }
    }
}