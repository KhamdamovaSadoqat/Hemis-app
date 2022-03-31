package com.software.hemis.presenter

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.hemis.R
import com.example.hemis.databinding.ActivityMainBinding
import com.software.hemis.utils.*
import com.software.hemis.utils.data.Authorization
import com.software.hemis.utils.data.Constants
import com.software.hemis.utils.data.SharedPref
import com.software.hemis.utils.notification.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPref: SharedPref

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.mobile_navigation)

        // Internet Connection Checker Start

        val connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this) { isNetworkAvailable ->
            isNetworkAvailable?.let {
                if (it)
                    Toast.makeText(this, "Internet Available", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show()
            }
        }

        // Internet Connection Checker End

        // Notification Start

//        scheduleNotification("Hemis_1", "Hemis_Message_1", "channel1","1",1648733220000)
//        scheduleNotification("Hemis_2", "Hemis_Message_2", "channel2","2",1648733225000)
//        scheduleNotification("Hemis_3", "Hemis_Message_3", "channel3","3",1648733230000)
//        scheduleNotification("Hemis_4", "Hemis_Message_4", "channel4","4",1648733235000)
//        scheduleNotification("Hemis_5", "Hemis_Message_5", "channel5","5",1648733240000)


        // Notification End

        //observing if the user is still logged in, if not navigating to login fragment to log in
        Authorization.getUnAuthorized().observe(this) {
            if (!it) navController.navigate(R.id.loginFragment)
        }

        if (!sharedPref.token.isNullOrEmpty()) graph.setStartDestination(R.id.splashFragment)
        else if (sharedPref.isFirstOpen == true) graph.setStartDestination(R.id.universityFragment)

        if (sharedPref.universityUrl != "") Constants.universityUrl = sharedPref.universityUrl!!

        navController.graph = graph

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.navigation_table ||
                destination.id == R.id.navigation_deadline ||
                destination.id == R.id.navigation_chat ||
                destination.id == R.id.navigation_other ||
                destination.id == R.id.navigation_subject
            ) {
                show()
            } else {
                hide()
            }
        }
        binding.navView.setupWithNavController(navController)
    }

    // cras
    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification(title: String, message: String, notificationChannelId: String, id: String, timer: Long) {
        val intent = Intent(this, Notification::class.java)

        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = timer

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun cancelReminderAlarm(context: Context) {
        val intent = Intent(context, Notification::class.java)
        val pIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.cancel(pIntent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun cancelAlarm(context: Context){
        cancelReminderAlarm(context)
    }

    // cras

    private fun hide() {
        binding.navView.animate().translationY(binding.navView.height.toFloat())
        binding.navView.gone()
    }

    private fun show() {
        binding.navView.animate().translationY(0f)
        binding.navView.visible()
    }

}
