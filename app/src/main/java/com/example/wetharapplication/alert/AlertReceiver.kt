package com.example.wetharapplication.alert

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
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


class AlertReceiver : BroadcastReceiver() {
    lateinit var response:MyResponse
    override fun onReceive(context: Context?, intent: Intent?) {

        var id = intent?.getIntExtra("id",0)

        var repo =
            Repository.getInstance(WeatherClient.getInstance(), ConcreteLocalSource.getInstance(context!!))


        CoroutineScope(Dispatchers.IO).launch {
             repo.getDataFromApi(31.1, 31.5, "metric", "en")
                 .collect {
                     response = it
                val i = Intent(context, AlertFragment::class.java)
                intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                val pendingIntent = PendingIntent.getActivity(context, 0, i, 0)
                val builder = NotificationCompat.Builder(context, id.toString())
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentTitle(response.timezone)
                   // .setContentText(response.alerts.get(0).description)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)

                val notificationManger = NotificationManagerCompat.from(context)
                     if (response.alerts.size!=0&&!response.alerts.isEmpty()){
                         builder.setContentText(response.alerts.get(0).description)
                     }else{
                         builder.setContentText("The Weather Is Good Today , Enjoy with it ")
                     }
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