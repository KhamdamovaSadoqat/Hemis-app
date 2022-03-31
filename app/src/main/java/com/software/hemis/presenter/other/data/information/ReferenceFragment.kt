package com.software.hemis.presenter.other.data.information

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.example.hemis.R
import com.example.hemis.databinding.FragmentReferenceBinding
import com.software.hemis.data.comman.base.BindingFragment
import com.software.hemis.utils.invisible
import com.software.hemis.utils.visible
import java.io.File

class ReferenceFragment : BindingFragment<FragmentReferenceBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentReferenceBinding::inflate

    private var downloaded = false
    private val path = "/storage/emulated/0"
    private val fileName = "fp_dc_setup_guide.pdf"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpView()
    }

    private fun setUpToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpView() {
        binding.cardApply.setOnClickListener {
            binding.clNotFound.invisible()
            binding.mcvDetails.visible()
        }
        if (downloaded){
            binding.fabDownload.progress = 0F
            binding.fabDownload.mBitmap = ContextCompat.getDrawable(requireContext(), R.drawable.ic_downloaded)!!.toBitmap()
        }
        binding.fabDownload.setOnClickListener {
            if(downloaded) readFile("$path/$fileName")
            else getFIleFromInternet()
        }
    }

    //need to remove
    private fun getFIleFromInternet() {
        val url = "https://download.support.xerox.com/pub/docs/FlowPort2/userdocs/any-os/en/fp_dc_setup_guide.pdf"

        val config = PRDownloaderConfig.newBuilder()
            .setReadTimeout(30000)
            .setConnectTimeout(30000)
            .build()
        PRDownloader.initialize(requireContext(), config)

        download(url, fileName)
    }

    private fun readFile(filePath: String) {
        val file = File(filePath)
        val map: MimeTypeMap = MimeTypeMap.getSingleton()
        val ext: String = MimeTypeMap.getFileExtensionFromUrl(file.name)
        var type: String? = map.getMimeTypeFromExtension(ext)

        if (type == null) type = "*/*"

        val intent = Intent(Intent.ACTION_VIEW)
        val data: Uri = FileProvider.getUriForFile(
            requireContext(),
            requireContext().applicationContext.packageName.toString() + ".provider",
            file
        )

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(data, type)

        startActivity(intent)
    }

    private fun download(url: String, fileName: String) {
        PRDownloader.download(
            url,
            path,
            fileName
        )
            .build()
            .setOnProgressListener {
                // Update the progress
                binding.fabDownload.progress = it.currentBytes  / it.totalBytes.toFloat()
                binding.fabDownload.mBitmap = ContextCompat.getDrawable(requireContext(), R.drawable.ic_close)!!.toBitmap()
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    Log.d("-------------", "onDownloadComplete: ")
                    // Update the progress bar to show the completeness
                    binding.fabDownload.progress = 0F
                    binding.fabDownload.mBitmap = ContextCompat.getDrawable(requireContext(), R.drawable.ic_downloaded)!!.toBitmap()

//                    Log.d("-------------", "onDownloadComplete: path: ${requireContext().filesDir.absolutePath}")
                    // Read the file
                    downloaded = true
                    readFile("$path/$fileName")
                }

                override fun onError(error: com.downloader.Error?) {
                    Toast.makeText(requireContext(), "Failed to download the $url", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }
}