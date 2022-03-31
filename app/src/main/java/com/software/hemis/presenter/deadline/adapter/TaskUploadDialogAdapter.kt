package com.software.hemis.presenter.deadline.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hemis.databinding.ItemUploadFileDialogBinding
import com.software.hemis.utils.AutoUpdatableAdapter
import com.software.hemis.utils.data.Constants
import java.io.File
import kotlin.properties.Delegates

class TaskUploadDialogAdapter(private val onClick: TaskUploadDialogOnClick) :
    RecyclerView.Adapter<TaskUploadDialogAdapter.VH>(),
    AutoUpdatableAdapter {

    var fileName: List<String> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    var fileUrl: List<Uri> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    class VH(private val binding: ItemUploadFileDialogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(fileName: String, fileUrl: Uri, position: Int, onClick: TaskUploadDialogOnClick) {
            binding.tvData.text = fileName
            val file = File("${Constants.FILE_STORE_PACKAGE}/$fileName")
            binding.ivClose.setOnClickListener {
                onClick.onClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemUploadFileDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false), )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(fileName[position], fileUrl[position], position, onClick)
    }

    override fun getItemCount(): Int = fileName.size

}