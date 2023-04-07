package com.example.wetharapplication.alert

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.wetharapplication.R
import com.example.wetharapplication.database.ConcreteLocalSource
import com.example.wetharapplication.model.MyResponse
import com.example.wetharapplication.model.Repository
import com.example.wetharapplication.network.WeatherClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AlertReceiver : BroadcastReceiver() {
    var LAYOUT_FLAG = 0
    lateinit var response:MyResponse
    lateinit var description: String

    override fun onReceive(context: Context?, intent: Intent?) {
        var id = intent?.getIntExtra("id",0)
        var notificationType  =intent?.getStringExtra("notification")
        var lat = intent?.getDoubleExtra("lat",31.0)
        var long = intent?.getDoubleExtra("long",31.0)

        var repo =
            Repository.getInstance(WeatherClient.getInstance(), ConcreteLocalSource.getInstance(context!!))


        CoroutineScope(Dispatchers.IO).launch {
            repo.getDataFromApi(lat!!, long!!, "metric", "en")
                .collect {
                    response = it
                    if (response.alerts.size != 0 && !response.alerts.isEmpty()) {
                        description = response.alerts.get(0).description.toString()
                    } else {
                        description = "The Weather Is Good Today , Enjoy with it "
                    }
                    if( notificationType  == "alarm"){
                        setAlarm(context,description)
                    }else {
                        val i = Intent(context, AlertFragment::class.java)
                        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        val pendingIntent = PendingIntent.getActivity(context, 0, i, 0)
                        val builder = NotificationCompat.Builder(context, id.toString())
                            .setSmallIcon(R.drawable.baseline_access_alarm_24)
                            .setContentTitle(response.timezone)
                            .setAutoCancel(true)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setContentIntent(pendingIntent)
                            .setContentText(description)
                            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        val notificationManger = NotificationManagerCompat.from(context)

                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {


                        }
                        notificationManger.notify(123, builder.build())

                    }
                    }


                }
    }

    private suspend fun setAlarm(context: Context , description : String) {
        LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else
            WindowManager.LayoutParams.TYPE_PHONE

        val mp = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI)

        val view: View = LayoutInflater.from(context).inflate(R.layout.alarm_daiolge, null, false)
        val dismissBtn = view.findViewById(R.id.btn_alarm_dismiss) as Button
         val textView = view.findViewById(R.id.alarm_desc) as TextView
        val layoutParams =
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        layoutParams.gravity = Gravity.TOP

        val windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager

        withContext(Dispatchers.Main) {
            windowManager.addView(view, layoutParams)
            view.visibility = View.VISIBLE
             textView.text = description
        }

        mp.start()
        mp.isLooping = true
       dismissBtn.setOnClickListener {
           mp?.release()
             windowManager.removeView(view)
         }

    }
}