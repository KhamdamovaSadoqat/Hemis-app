package com.software.hemis.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.example.hemis.R
import com.software.hemis.presenter.schedule.adapter.CheckTime
import com.software.hemis.utils.customView.ProgressView
import com.software.hemis.utils.customView.ProgressViewDownload
import com.software.hemis.utils.data.Constants
import java.io.File
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.run


fun getNavOptions(): NavOptions.Builder {
    return NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
}

class NetworkHelper constructor(private val context: Context) {
    fun isConnected(): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val networkCapability = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapability) ?: return false

            result = when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return result
    }
}

interface AutoUpdatableAdapter {
    fun <T> RecyclerView.Adapter<*>.autoNotify(
        oldList: List<T>,
        newList: List<T>,
        compare: (T, T) -> Boolean,
    ) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return compare(oldList[oldItemPosition], newList[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }

            override fun getOldListSize() = oldList.size
            override fun getNewListSize() = newList.size
        })
        diff.dispatchUpdatesTo(this)
    }
}

fun checkingTime(time: String, date: Int): CheckTime {
    //state: 1-->passed
    //       2-->currently running
    //       3-->upcoming
    //time: 8:30
    //date 1638144000 in seconds
    //date 1638144000000 in millseconds
    //8:30     216000000
    val hhMm = time.split(":") //[08], [30]

    val calendar = Calendar.getInstance() // creates a new calendar instance
    val startTime = Calendar.getInstance()
    val endTime = Calendar.getInstance()
    //                        //hour in seconds         // minut in seconds
    val timeInSeconds = date + (hhMm[0].toInt() - 5) * 3600 + hhMm[1].toInt() * 60  //seconds
    startTime.timeInMillis = (timeInSeconds).toLong() * 1000
    endTime.timeInMillis = (timeInSeconds).toLong() * 1000 + 4800000

    //Wed Mar 09 14:50:00 GMT+05:00 2022
    val rEndTime = endTime.time.toString().split(" ")
    val rTime = rEndTime[3].split(":")
    return when {
        calendar.timeInMillis < startTime.timeInMillis -> CheckTime(
            "${hhMm[0]}:${hhMm[1]}",
            "${rTime[0]}:${rTime[1]}",
            3
        )
        calendar.timeInMillis > endTime.timeInMillis -> CheckTime(
            "${hhMm[0]}:${hhMm[1]}",
            "${rTime[0]}:${rTime[1]}",
            1
        )
        else -> CheckTime("${hhMm[0]}:${hhMm[1]}", "${rTime[0]}:${rEndTime[1]}", 2)
    }
}

fun weekStartEnd(weekStart: Int, weekEnd: Int): WeekTime {
    //startTime: 1646611200
    //endTime:   1647043200
    //a day == 86400000

    val calendar = Calendar.getInstance() // creates a new calendar instance
    val startTime = Calendar.getInstance()
    val endTime = Calendar.getInstance()
    val week = arrayListOf<IsToday>()
    var todayPos = -1

    startTime.timeInMillis = weekStart.toLong() * 1000
    endTime.timeInMillis = weekEnd.toLong() * 1000

//    //Wed Mar 09 14:50:00 GMT+05:00 2022
//    val time = endTime.time.toString().split(" ")
//    val rTime = time[3].split(":")

    DateFormat.format("d", startTime).toString()

    for (days in 0..6) {
        if (DateUtils.isToday(startTime.timeInMillis)) {
            week.add(IsToday(DateFormat.format("d", startTime).toString(),
                true,
                (startTime.timeInMillis / 1000).toInt()))
        } else week.add(IsToday(DateFormat.format("d", startTime).toString(),
            false,
            (startTime.timeInMillis / 1000).toInt()))
        startTime.timeInMillis += 86400000
    }

    return WeekTime(
        week,
        DateFormat.format("MM", startTime).toString(),
        DateFormat.format("MM", endTime).toString(),
    )
}

fun getTodayInSeconds(): Int {
    val calendar = Calendar.getInstance()
    val today: Long = calendar.timeInMillis

    return ((today -
            ((DateFormat.format("HH", today).toString().toInt() - 5) * 3600000) -
            (DateFormat.format("mm", today).toString().toInt() * 60000) -
            (DateFormat.format("ss", today).toString().toInt() * 1000)
            ) / 1000).toInt()
}

fun deadlineType(deadline: Int): Int {
    val current = (System.currentTimeMillis() / 1000).toInt()
    return when {
        deadline - current >= 604800 -> Constants.NEW_TASK
        deadline - current >= 518400 -> Constants.LESS_THAN_SEVEN_DAYS
        deadline - current >= 432000 -> Constants.LESS_THAN_SIX_DAYS
        deadline - current >= 345600 -> Constants.LESS_THAN_FIVE_DAYS
        deadline - current >= 259200 -> Constants.LESS_THAN_FOUR_DAYS
        deadline - current >= 172800 -> Constants.LESS_THAN_THREE_DAYS
        deadline - current >= 86400 -> Constants.LESS_THAN_TWO_DAYS
        deadline - current > 0 -> Constants.WAITING_UPLOAD
        else -> Constants.DEADLINE_PASSED
    }
}

fun secondsToDateAndTime(deadline: Int): String{
    // deadline:
    // string: 18.03.2022 23:00
    val time = Calendar.getInstance()
    time.timeInMillis = deadline.toLong() * 1000
    return DateFormat.format("dd.MM.yyyy HH:mm", time).toString()
}

fun secondsToDate(deadline: Int): String {
    // deadline:
    // string: 18.03.2022
    val time = Calendar.getInstance()
    time.timeInMillis = deadline.toLong() * 1000
    return DateFormat.format("dd.MM.yyyy", time).toString()
}

data class WeekTime(
    var week: List<IsToday>,
    var star: String,
    var end: String,
)

data class IsToday(
    var day: String,
    var today: Boolean,
    var todayInSeconds: Int,
)

fun readFile(filePath: String, context: Context) {
    val file = File(filePath)
    val map: MimeTypeMap = MimeTypeMap.getSingleton()
    val ext: String = MimeTypeMap.getFileExtensionFromUrl(file.name)
    var type: String? = map.getMimeTypeFromExtension(ext)

    if (type == null) type = "*/*"

    val intent = Intent(Intent.ACTION_VIEW)
    val data: Uri = FileProvider.getUriForFile(
        context,
        context.applicationContext.packageName.toString() + ".provider",
        file
    )

    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.setDataAndType(data, type)

    context.startActivity(intent)
}

fun createFolder() {
    val folder = File("/storage//emulated//0", "Download")
    if (!folder.exists()) {
        folder.mkdir()
    }
}

fun getFileTeacherFromInternet(
    url: String,
    fileName: String,
    context: Context,
    viewDownload: ProgressViewDownload,
) {
    val config = PRDownloaderConfig.newBuilder()
        .setReadTimeout(30000)
        .setConnectTimeout(30000)
        .build()
    PRDownloader.initialize(context, config)
    downloadFileTeacher(url, fileName, context, viewDownload)
}

private fun downloadFileTeacher(
    url: String,
    fileName: String,
    context: Context,
    viewDownload: ProgressViewDownload,
) {
    PRDownloader.download(url, Constants.FILE_STORE_PACKAGE, fileName)
        .build()
        .setOnProgressListener {
            // Update the progress
            viewDownload.progress = it.currentBytes / it.totalBytes.toFloat()
            viewDownload.mBitmap =
                ContextCompat.getDrawable(context, R.drawable.ic_close)!!.toBitmap()
        }
        .start(object : OnDownloadListener {
            override fun onDownloadComplete() {
                // Update the progress bar to show the completeness
                viewDownload.progress = 0F
                viewDownload.mBitmap =
                    ContextCompat.getDrawable(context, R.drawable.ic_downloaded)!!
                        .toBitmap()
                // Read the file
                readFile("${Constants.FILE_STORE_PACKAGE}/$fileName", context)
            }

            override fun onError(error: com.downloader.Error?) {
                Log.d("", "------------- onError: ${error?.connectionException?.message}")
                Toast.makeText(
                    context,
                    "Failed to download the $url",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
}

fun getFileStudentFromInternet(
    url: Uri,
    fileName: String,
    context: Context,
    view: ProgressView,
) {
    val config = PRDownloaderConfig.newBuilder()
        .setReadTimeout(30000)
        .setConnectTimeout(30000)
        .build()
    PRDownloader.initialize(context, config)
    downloadFileStudent(url.path!!, fileName, context, view)
}

private fun downloadFileStudent(
    url: String,
    fileName: String,
    context: Context,
    view: ProgressView,
) {
    PRDownloader.download(url, Constants.FILE_STORE_PACKAGE, fileName)
        .build()
        .setOnProgressListener {
            // Update the progress
            view.progress = it.currentBytes / it.totalBytes.toFloat()
            view.mBitmap =
                ContextCompat.getDrawable(context, R.drawable.ic_close)!!.toBitmap()
        }
        .start(object : OnDownloadListener {
            override fun onDownloadComplete() {
                // Update the progress bar to show the completeness
                view.progress = 0F
                view.mBitmap =
                    ContextCompat.getDrawable(context, R.drawable.ic_downloaded)!!
                        .toBitmap()
                // Read the file
                readFile("${Constants.FILE_STORE_PACKAGE}/$fileName", context)
            }

            override fun onError(error: com.downloader.Error?) {
                Toast.makeText(
                    context,
                    "Failed to download the $url",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
}