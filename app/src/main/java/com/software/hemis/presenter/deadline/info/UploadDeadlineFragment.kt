package com.software.hemis.presenter.deadline.info

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.example.hemis.R
import com.example.hemis.databinding.DialogUploadTaskBinding
import com.example.hemis.databinding.FragmentUploadDeadlineBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.software.hemis.data.comman.base.BindingFragment
import com.software.hemis.presenter.deadline.DeadlineViewModel
import com.software.hemis.presenter.deadline.TaskUploadState
import com.software.hemis.presenter.deadline.adapter.TaskUploadDialogAdapter
import com.software.hemis.presenter.deadline.adapter.TaskUploadDialogOnClick
import com.software.hemis.presenter.deadline.adapter.TaskUploadRecyclerAdapter
import com.software.hemis.utils.*
import com.software.hemis.utils.data.Constants
import com.software.hemis.utils.data.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class UploadDeadlineFragment : BindingFragment<FragmentUploadDeadlineBinding>(),
    TaskUploadDialogOnClick {

    @Inject
    lateinit var pref: SharedPref
    private lateinit var adapter: TaskUploadRecyclerAdapter
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUploadDeadlineBinding::inflate
    private val viewModel: DeadlineViewModel by viewModels()
    private val args: UploadDeadlineFragmentArgs by navArgs()

    private lateinit var uploadTaskDialogBinding: DialogUploadTaskBinding
    private var uploadFileDialog: BottomSheetDialog? = null
    private lateinit var adapterDialog: TaskUploadDialogAdapter

    private val fileNameList = arrayListOf<String>()
    private val fileUrlList = arrayListOf<Uri>()

    private var url = ""
    private var fileName = ""
    private lateinit var uri: Uri

    var parts = arrayListOf<MultipartBody.Part>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        setUpViews()
        taskWithDetailsAndSubmission(args.subjectId, pref.currentSemester!!, args.taskId)
        subject(args.subjectId)
    }

    private fun setUpViews() {
        setUpToolBar()
        uploadTaskDialogBinding =
            DialogUploadTaskBinding.inflate(LayoutInflater.from(requireContext()))

        adapter = TaskUploadRecyclerAdapter { position -> }
        adapterDialog = TaskUploadDialogAdapter(this)
        binding.content.rvUploadedFiles.adapter = adapter
        uploadTaskDialogBinding.rvUploadedFiles.adapter = adapterDialog
        binding.content.fabDownload.setOnClickListener {
            createFolder()
            //checking where the file has or not
            val file = File("${Constants.FILE_STORE_PACKAGE}/$fileName")
            if (file.exists()) {
                readFile(
                    "${Constants.FILE_STORE_PACKAGE}/$fileName",
                    requireContext()
                )
            } else {
                getFileTeacherFromInternet(
                    url,
                    fileName,
                    requireContext(),
                    binding.content.fabDownload)
            }
        }

        binding.content.btnUploadFile.setOnClickListener {
            openFile()
        }
        uploadTaskDialogBinding.btnSubmit.setOnClickListener {
            for (file in fileUrlList.indices) {
                parts.add(prepareFilePart(fileUrlList[file]))
            }
            taskUpload(args.taskId, parts)
        }
    }

    private fun setUpToolBar() {
        binding.collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK)
        binding.collapsingToolbar.setExpandedTitleColor(Color.BLACK)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun taskWithDetailsAndSubmission(subjectId: Int, semesterId: Int, taskId: Int) {
        viewModel.getTaskWithDetail(subjectId, semesterId, taskId)
            .observe(this) { task ->
                binding.content.tvTaskName.text = task.taskEntity.name
                binding.content.tvTaskField.text = task.taskEntity.trainingType
                binding.content.tvTaskDeadline.text = secondsToDateAndTime(task.taskEntity.deadline)

                binding.content.tvMaxBall.text = task.taskEntity.maxBall.toString()
                binding.content.tvBallPercent.text =
                    requireContext().getString(
                        R.string.ball_percent,
                        0,
                        "%")
                binding.content.tvDataDesc.text = task.taskEntity.comment
                val (key, value) = task.taskDetailEntity.fileUrl.entries.iterator().next()
                binding.content.tvHomeTaskName.text = value
                if (value != null) {
                    fileName = value
                }
                if (key != null) {
                    url = key
                }
                when (task.taskEntity.taskStatusCode) {
                    Constants.GIVEN ->
                        when (deadlineType(task.taskEntity.deadline)) {
                            Constants.WAITING_UPLOAD -> waitingUpload()
                            Constants.LESS_THAN_TWO_DAYS -> withinAWeek(requireContext().getString(R.string.less_than_week,
                                2))
                            Constants.LESS_THAN_THREE_DAYS -> withinAWeek(requireContext().getString(
                                R.string.less_than_week,
                                3))
                            Constants.LESS_THAN_FOUR_DAYS -> withinAWeek(requireContext().getString(
                                R.string.less_than_week,
                                4))
                            Constants.LESS_THAN_FIVE_DAYS -> withinAWeek(requireContext().getString(
                                R.string.less_than_week,
                                5))
                            Constants.LESS_THAN_SIX_DAYS -> withinAWeek(requireContext().getString(R.string.less_than_week,
                                6))
                            Constants.LESS_THAN_SEVEN_DAYS -> withinAWeek(requireContext().getString(
                                R.string.less_than_week,
                                7))
                            Constants.NEW_TASK -> newTask()
                            Constants.DEADLINE_PASSED -> deadlinePassed()

                        }
                    Constants.UPLOADED -> uploaded()
                    Constants.MARKED -> marked()
                }

                val file = File("${Constants.FILE_STORE_PACKAGE}/$fileName")
                if (file.exists()) {
                    binding.content.fabDownload.progress = 0F
                    binding.content.fabDownload.mBitmap =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_downloaded)!!
                            .toBitmap()
                } else {
                    binding.content.fabDownload.progress = 0F
                    binding.content.fabDownload.mBitmap = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_download
                    )!!.toBitmap()
                }

                if (task.taskDetailEntity.markedTaskId != -1) getTaskDetailsWithSubmission(task.taskDetailEntity.markedTaskId)
            }
    }

    private fun getTaskDetailsWithSubmission(taskId: Int) {
        viewModel.getTaskDetailsWithSubmission(taskId).observe(this) { submission ->
            binding.content.tvMarkedBall.text = submission.mark.toString()

            for (file in submission.file.entries) {
                val (key, value) = submission.file.entries.iterator().next()
                fileNameList.add(value ?: "")
                fileUrlList.add(key!!.toUri())
            }

            adapter.fileName = fileNameList
            adapter.fileUrl = fileUrlList
            binding.content.rvUploadedFiles.visible()
            binding.content.ivFileNotFound.gone()
            binding.content.tvFileNotFound.gone()
        }
    }

    private fun subject(subjectId: Int) {
        viewModel.getSubject(subjectId).observe(this) { subject ->
            binding.collapsingToolbar.title = subject.subjectName
            binding.content.tvSubjectName.text = subject.subjectName
        }
    }

    private fun taskUpload(taskId: Int, filePart: List<MultipartBody.Part>) {
        viewModel.taskUpload(taskId, filePart)
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.mTaskUploadState.collectLatest {
                handleStateChange(it)
            }
        }
    }

    private fun handleStateChange(state: TaskUploadState) {
        when (state) {
            is TaskUploadState.Init -> Unit
            is TaskUploadState.ErrorTaskUpload -> {
                Toast.makeText(requireContext(), state.rawResponse.error, Toast.LENGTH_SHORT).show()
                binding.content.rvUploadedFiles.gone()
                binding.content.ivFileNotFound.visible()
                binding.content.tvFileNotFound.visible()
            }
            is TaskUploadState.SuccessTaskUpload -> {
                clearCacheFiles()
                viewModel.getTaskDetail(args.taskId)
            }
            is TaskUploadState.ShowToast -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
            }
            is TaskUploadState.IsLoading -> {
                dismissUploadFileDialog()
                adapter.fileName = fileNameList
                adapter.fileUrl = fileUrlList
                binding.content.rvUploadedFiles.visible()
                binding.content.ivFileNotFound.gone()
                binding.content.tvFileNotFound.gone()
            }
        }
    }

    private fun waitingUpload() {
        binding.content.tvCardStatus.text =
            requireContext().getString(R.string.task_waiting)
        binding.content.tvCardStatus.setTextColor(ContextCompat.getColor(
            requireContext(),
            R.color.white))
        binding.content.ivIcon.setImageDrawable(ContextCompat.getDrawable(
            requireContext(),
            R.drawable.img_fire))
        binding.content.cardStatus.background =
            ContextCompat.getDrawable(requireContext(),
                R.drawable.bg_task_waiting)
    }

    private fun withinAWeek(textStatus: String) {
        binding.content.tvCardStatus.text = textStatus
        binding.content.tvCardStatus.setTextColor(ContextCompat.getColor(
            requireContext(),
            R.color.yellow))
        binding.content.ivIcon.setImageDrawable(ContextCompat.getDrawable(
            requireContext(),
            R.drawable.img_warning))
        binding.content.cardStatus.background =
            ContextCompat.getDrawable(requireContext(),
                R.drawable.bg_task_less_than_three_days)
    }

    private fun uploaded() {
        binding.content.tvCardStatus.text =
            requireContext().getString(R.string.task_uploaded)
        binding.content.tvCardStatus.setTextColor(ContextCompat.getColor(
            requireContext(),
            R.color.green))
        binding.content.ivIcon.setImageDrawable(ContextCompat.getDrawable(
            requireContext(),
            R.drawable.img_done))
        binding.content.cardStatus.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_task_uploaded)
    }

    private fun marked() {
        binding.content.tvCardStatus.text =
            requireContext().getString(R.string.task_marked)
        binding.content.tvCardStatus.setTextColor(ContextCompat.getColor(
            requireContext(),
            R.color.purple))
        binding.content.ivIcon.setImageDrawable(ContextCompat.getDrawable(
            requireContext(),
            R.drawable.img_marked))
        binding.content.cardStatus.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_task_marked)
    }

    private fun deadlinePassed() {
        binding.content.tvCardStatus.text =
            requireContext().getString(R.string.deadline_passed)
        binding.content.tvCardStatus.setTextColor(ContextCompat.getColor(
            requireContext(),
            R.color.red))
        binding.content.ivIcon.setImageDrawable(ContextCompat.getDrawable(
            requireContext(),
            R.drawable.img_deadline))
        binding.content.cardStatus.background =
            ContextCompat.getDrawable(requireContext(),
                R.drawable.bg_task_deadine_passed)
    }

    private fun newTask() {
        binding.content.tvCardStatus.text = requireContext().getString(R.string.new_task)
        binding.content.tvCardStatus.setTextColor(ContextCompat.getColor(requireContext(),
            R.color.light_blue_darker))
        binding.content.ivIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(),
            R.drawable.img_new))
        binding.content.cardStatus.background =
            ContextCompat.getDrawable(requireContext(),
                R.drawable.bg_task_new)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                fileNameList.clear()
                fileUrlList.clear()
                binding.content.rvUploadedFiles.invalidate()

                // for multiple selection
                if (result.data!!.clipData != null) {
                    for (i in 0 until result.data!!.clipData!!.itemCount) {
                        val uri: Uri = result.data!!.clipData!!.getItemAt(i).uri
                        val list = result.data!!.clipData!!.getItemAt(i).uri.path!!.split("/")
                        fileNameList.add(list[list.size - 1])
                        fileUrlList.add(uri)
                    }

                } else { // for single selection
                    uri = result.data!!.data!!
                    val list = result.data!!.data!!.path!!.split("/")
                    fileNameList.add(list[list.size - 1])
                    fileUrlList.add(uri)
                }
                adapterDialog.fileName = fileNameList
                adapterDialog.fileUrl = fileUrlList
                adapterDialog.notifyItemRangeInserted(0, fileNameList.size)
                (uploadTaskDialogBinding.root.parent as? ViewGroup)?.removeView(
                    uploadTaskDialogBinding.root)

                showUploadFileDialog()
            }
        }

    private fun getFileFromUri(
        contentResolver: ContentResolver,
        uri: Uri,
        directory: File,
        extension: String?,
    ): File {
        val file = File.createTempFile(directory.nameWithoutExtension,
            ".${extension?.split("/")?.get(1)}", directory)
        file.outputStream().use {
            contentResolver.openInputStream(uri)?.copyTo(it)
        }
        return file
    }

    private fun clearCacheFiles() {
        val cacheDir = requireContext().cacheDir
        if (cacheDir?.isDirectory == true) {
            val listFiles = cacheDir.listFiles()
            if (listFiles?.isEmpty() == true) return
            listFiles?.forEach {
                it.delete()
            }
        }
    }

    private fun prepareFilePart(fileUri: Uri): MultipartBody.Part {
        val extension = requireContext().contentResolver.getType(fileUri)
        val file = getFileFromUri(requireContext().contentResolver,
            fileUri, requireContext().cacheDir,
            extension)
        val requestBody: RequestBody = file.asRequestBody("*/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("filename[]", file.name, requestBody)
    }

    private fun openFile() {
        val mimetypes = arrayOf(
            "image/png",
            "image/jpg",
            "image/jpeg",
            "application/xls",
            "application/pdf",
            "application/xlsx",
            "application/doc",
            "application/docx",
            "application/ppt",
            "text/plain",
            "application/zip",
            "application/rar")
        val filesIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            putExtra(Intent.EXTRA_TITLE, true)
            putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
            type = "*/*"
        }
        resultLauncher.launch(filesIntent)
    }

    private fun showUploadFileDialog() {
        uploadFileDialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)
        uploadFileDialog!!.setContentView(uploadTaskDialogBinding.root)
        uploadFileDialog!!.show()
    }

    private fun dismissUploadFileDialog() {
        if (uploadFileDialog!!.isShowing) {
            uploadFileDialog!!.dismiss()
        }
        uploadFileDialog!!.dismiss()
    }

    override fun onClick(position: Int) {
        fileNameList.removeAt(position)
        fileUrlList.removeAt(position)
        adapterDialog.notifyItemRemoved(position)
        adapterDialog.notifyItemRangeRemoved(position, fileUrlList.size)
        if (fileNameList.size == 0) dismissUploadFileDialog()
    }
}