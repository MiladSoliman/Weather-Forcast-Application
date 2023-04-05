package com.example.wetharapplication.alert

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.getBroadcast
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wetharapplication.databinding.FragmentAlertBinding


class AlertFragment : Fragment() {
    lateinit var binding:FragmentAlertBinding
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setAlarm()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      var alertDailog = AlertDailog()


       binding.alertFAB.setOnClickListener {
           activity?.supportFragmentManager?.let { it1 -> alertDailog.show(it1,"Milad") }
        }
    }




    @SuppressLint("WrongConstant")
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name: CharSequence = "Mina Nageh"
            val description = "Cloudy Sky"
            val importance = NotificationManager.IMPORTANCE_MAX
            val channel = NotificationChannel("Easy Weathy",name,importance)
            channel.description = description
            val notificationManager = activity?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }


    private fun setAlarm(){
        alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(),AlertReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(),0,intent,0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,1680653520000,pendingIntent)

    }


}