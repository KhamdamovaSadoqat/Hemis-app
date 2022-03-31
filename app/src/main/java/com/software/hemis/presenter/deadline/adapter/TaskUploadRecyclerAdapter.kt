package com.software.hemis.presenter.deadline.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.example.hemis.R
import com.example.hemis.databinding.ItemUploadFileBinding
import com.software.hemis.utils.AutoUpdatableAdapter
import com.software.hemis.utils.createFolder
import com.software.hemis.utils.data.Constants
import com.software.hemis.utils.getFileStudentFromInternet
import com.software.hemis.utils.readFile
import java.io.File
import kotlin.properties.Delegates

class TaskUploadRecyclerAdapter(private val itemClickListener: (Int) -> Unit) :
    RecyclerView.Adapter<TaskUploadRecyclerAdapter.VH>(),
    AutoUpdatableAdapter {

    var fileName: List<String> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    var fileUrl: List<Uri> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    class VH(private val binding: ItemUploadFileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(fileName: String, fileUrl: Uri) {
            binding.tvData.text = fileName
            val file = File("${Constants.FILE_STORE_PACKAGE}/$fileName")
            Log.d("VH", "------------- onBind: file exists: ${file.exists()}")
            Log.d("VH", "------------- onBind: file exists: ${file.absolutePath}")
            if (file.exists()) {
                binding.fabDownload.progress = 0F
                binding.fabDownload.mBitmap =
                    ContextCompat.getDrawable(binding.root.context, R.drawable.ic_downloaded)!!
                        .toBitmap()
            } else {
                binding.fabDownload.progress = 0F
                binding.fabDownload.mBitmap = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.ic_download
                )!!.toBitmap()
            }
            binding.fabDownload.setOnClickListener {
                createFolder()
                //checking where the file has or not
                val file = File("${Constants.FILE_STORE_PACKAGE}/$fileName")
                if (file.exists()){
                    binding.fabDownload.progress = 0F
                    binding.fabDownload.mBitmap =
                        ContextCompat.getDrawable(binding.root.context, R.drawable.ic_downloaded)!!
                            .toBitmap()
                    readFile(
                        "${Constants.FILE_STORE_PACKAGE}/$fileName",
                        binding.root.context
                    )
                }
                else getFileStudentFromInternet(
                    fileUrl,
                    fileName,
                    binding.root.context,
                    binding.fabDownload
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemUploadFileBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(fileName[position], fileUrl[position])
        holder.itemView.setOnClickListener {
            itemClickListener.invoke(position)
        }
    }

    override fun getItemCount(): Int = fileName.size

}