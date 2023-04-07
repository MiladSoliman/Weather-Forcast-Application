package com.example.wetharapplication.alert

import android.annotation.SuppressLint
import android.app.*
import android.app.PendingIntent.getBroadcast
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wetharapplication.R
import com.example.wetharapplication.database.ConcreteLocalSource
import com.example.wetharapplication.databinding.FragmentAlertBinding
import com.example.wetharapplication.home.viewmodel.HomeViewModel
import com.example.wetharapplication.home.viewmodel.HomeViewModelFactory
import com.example.wetharapplication.model.Repository
import com.example.wetharapplication.network.WeatherClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.SimpleDateFormat


class AlertFragment : Fragment() , DeleteAlert {
    lateinit var binding: FragmentAlertBinding
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent
    lateinit var editToTime: TextView
    lateinit var editToDate: TextView
    lateinit var editFromTime: TextView
    lateinit var editFromDate: TextView
    var myAlert: MyAlert = MyAlert("", "", "", "", "", 0)
    lateinit var calenderFromTime : Calendar
    lateinit var alertModel : AlertViewModel
    lateinit var alertfactory : AlertViewModelFactory
    lateinit var myAlertList : List<MyAlert>
    private var lat: Double = 0.0
    private var long: Double = 0.0
    lateinit var se: SharedPreferences
    var startDay = 0
    var endDay = 0
    var startMonth=0
    var endMonth=0
    var startHour=0
    var endHour=0
    var startMinute=0
    var endMiunute=0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        se = activity?.getSharedPreferences("My Shared", Context.MODE_PRIVATE)!!
        lat = se.getFloat("lat", 31.0F)!!.toDouble()
        long = se.getFloat("long",31.0F)!!.toDouble()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var alertDailog = AlertDailog()


        binding.alertFAB.setOnClickListener {
            alertDialog()
        }

    }

    private fun requestOverAppPermission() {
        startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION),20)
    }

    override fun onResume() {
        super.onResume()
        alertfactory = AlertViewModelFactory(Repository.getInstance(WeatherClient.getInstance(), ConcreteLocalSource.getInstance(requireContext())))
        alertModel = ViewModelProvider(requireActivity(), alertfactory).get(AlertViewModel::class.java)

        alertModel._alerts.observe(viewLifecycleOwner){
            myAlertList = it
            binding.alertRecy.apply {
                adapter = AlertAdapter(myAlertList,this@AlertFragment)
                layoutManager = LinearLayoutManager(context)
            }

        }

    }


    @SuppressLint("WrongConstant")
    private fun createNotificationChannel(id:Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Weather Application"
            val description = "Channel For Weather"
            val importance = NotificationManager.IMPORTANCE_MAX
            val channel = NotificationChannel(id.toString(), name, importance)
            channel.description = description
            val notificationManager = activity?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }


    private fun setAlarm(time:Long , id:Int , type:String) {
        alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlertReceiver::class.java)
        intent.putExtra("notification", type)
        intent.putExtra("lat", lat)
        intent.putExtra("long", long)
      /*  intent.putExtra("id", id )
        pendingIntent = PendingIntent.getBroadcast(requireContext(), id, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)*/

        var days = (endDay - startDay) + ((endMonth - startMonth) * 30)
        Log.i("days", "" + days)
        var interval = 24 * 60 * 60 * 1000
        for (i in 0..days) {
            intent.putExtra("id", id+i )
            pendingIntent = PendingIntent.getBroadcast(requireContext(), id + i, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time + (i * interval), pendingIntent)
        }
    }



    fun alertDialog() {
        var dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_alert_dailog)
        val window: Window? = dialog.getWindow()
        //  window?.setBackgroundDrawableResource(R.color.transparent);
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        //window?.setBackgroundDrawableResource(R.color.transparent);
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()


        dialog.findViewById<CardView>(R.id.FromCard).setOnClickListener {
            showFromTimeAndDatePicker()
        }

        dialog.findViewById<CardView>(R.id.ToCard).setOnClickListener {
            showToTimeAndDatePicker()
        }


        editFromTime = dialog.findViewById<TextView>(R.id.edit_time)
        editFromDate = dialog.findViewById<TextView>(R.id.edit_date)
        editToTime = dialog.findViewById<TextView>(R.id.edit_to_time)
        editToDate = dialog.findViewById<TextView>(R.id.edit_to_date)
        dialog.findViewById<ImageView>(R.id.btn_cancel).setOnClickListener{
            dialog.dismiss()
        }


        dialog.findViewById<RadioButton>(R.id.notification_radio).setOnClickListener {
            myAlert.notifcation = "notification"
        }
        dialog.findViewById<RadioButton>(R.id.alaram_radio).setOnClickListener {
            myAlert.notifcation = "alarm"
        }



        dialog.findViewById<Button>(R.id.alarmset).setOnClickListener {
            if (myAlert.notifcation=="alarm"){
                if (!Settings.canDrawOverlays(requireContext())){
                    requestOverAppPermission()
                }
            }
            var id =generateUniqueIntValue(myAlert.startDay,myAlert.endDay,myAlert.startTime)
            createNotificationChannel(id)
            var time = calenderFromTime.timeInMillis
            setAlarm(time,id,myAlert.notifcation)
            alertModel.insertAlert(myAlert)
            Log.i("milad",myAlert.notifcation)
            dialog.dismiss()
        }

    }

    private fun updateToDateText(calenderDate: Calendar?) {
        val day = SimpleDateFormat("dd").format(calenderDate?.time)
        val month = SimpleDateFormat("MMM").format(calenderDate?.time)
        val year = SimpleDateFormat("YYYY").format(calenderDate?.time)
        myAlert.endDay = "$day/$month/$year"
        editToDate.text = "$day/$month/$year"


    }

    private fun updateToTime(calenderTime: Calendar?) {
        val format = SimpleDateFormat("hh:mm aa")
        val time = format.format(calenderTime?.time)
        myAlert.endTime = "$time"
        editToTime.text = "$time"
    }


    private fun updateFromTime(calenderTime: Calendar?) {
        val format = SimpleDateFormat("hh:mm aa")
        val time = format.format(calenderTime?.time)
        myAlert.startTime = "$time"
        editFromTime.text = "$time"

    }

    private fun updateFromDateText(calenderDate: Calendar?) {
        val day = SimpleDateFormat("dd").format(calenderDate?.time)
        val month = SimpleDateFormat("MMM").format(calenderDate?.time)
        val year = SimpleDateFormat("YYYY").format(calenderDate?.time)
        myAlert.startDay = "$day/$month/$year"
        editFromDate.text = "$day/$month/$year"

    }

    fun showFromTimeAndDatePicker() {
        calenderFromTime = Calendar.getInstance()
        val timePicker = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calenderFromTime .set(Calendar.HOUR_OF_DAY, hourOfDay)
            calenderFromTime .set(Calendar.MINUTE, minute)
            calenderFromTime .timeZone = TimeZone.getDefault()

            startHour = hourOfDay
            startMinute=minute

            updateFromTime(calenderFromTime )
        }
        val timePickerDialog = TimePickerDialog(
            requireContext(), timePicker, calenderFromTime .get(
                Calendar.HOUR_OF_DAY
            ), calenderFromTime .get(Calendar.MINUTE), false
        )

        val title2 = "Choose time"
        timePickerDialog.setTitle(title2)
        // datePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()

        val calenderDate = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

            calenderDate.set(Calendar.YEAR, year)
           calenderDate.set(Calendar.MONTH, month)
            calenderDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            startDay=dayOfMonth
            startMonth=month

            // val calenderTime = Calendar.getInstance()
            updateFromDateText(calenderDate)
        }

        val datePickerDialog = DatePickerDialog(
            requireContext(), datePicker, calenderDate.get(
                Calendar.YEAR
            ), calenderDate.get(Calendar.MONTH), calenderDate.get(Calendar.DAY_OF_MONTH)
        )

        val title = "Choose date"
        datePickerDialog.setTitle(title)
        // datePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        datePickerDialog.show()

    }


    fun showToTimeAndDatePicker() {
        val calenderTime = Calendar.getInstance()
        val timePicker = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calenderTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calenderTime.set(Calendar.MINUTE, minute)
            calenderTime.timeZone = TimeZone.getDefault()

            endHour=hourOfDay
            endMiunute = minute

            updateToTime(calenderTime)
        }
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            timePicker,
            calenderTime.get(Calendar.HOUR_OF_DAY),
            calenderTime.get(Calendar.MINUTE),
            false
        )

        val title2 = "Choose time"
        timePickerDialog.setTitle(title2)
        // datePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()


        val calenderDate = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

            calenderDate.set(Calendar.YEAR, year)
            calenderDate.set(Calendar.MONTH, month)
            calenderDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            endDay=dayOfMonth
            endMonth=month

            // val calenderTime = Calendar.getInstance()
            updateToDateText(calenderDate)
        }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            datePicker,
            calenderDate.get(Calendar.YEAR),
            calenderDate.get(Calendar.MONTH),
            calenderDate.get(Calendar.DAY_OF_MONTH)
        )

        val title = "Choose date"
        datePickerDialog.setTitle(title)
        // datePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        datePickerDialog.show()
    }


    fun generateUniqueIntValue(a: String, b: String, str: String): Int {
        val input = "$a$b$str"
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray(StandardCharsets.UTF_8))
        val truncatedHash = hash.copyOfRange(0, 4) // Truncate hash to 4 bytes
        return truncatedHash.fold(0) { acc, byte -> (acc shl 8) + (byte.toInt() and 0xff) }
    }

    override fun deleteAlert(myAlert: MyAlert) {
        alertModel.deletAlert(myAlert)
    }




  /*  private fun deleteCompletedNotification(data: MyUserAlert, i: Long,trigerTime:Long) {
        var hoursDelay=data.timeTo-data.timeFrom
        Log.i("time","$hoursDelay")
        var finalTrigger=hoursDelay+trigerTime
        val delteIntent  = Intent(requireActivity(), DeleteAlarmReciever::class.java)
        delteIntent.putExtra("alert",data.id+i.toInt())
        val deletePending = getBroadcast(requireContext(), data.id+i.toInt(), delteIntent, PendingIntent.FLAG_MUTABLE)
        Log.i("time","${data.id+i.toInt()}   id in fragment")
        alarmManager!!.setExact(AlarmManager.RTC_WAKEUP, finalTrigger, deletePending);
    }*/




   /* override fun onReceive(context: Context?, intent: Intent?) {
        val alertId=intent?.getIntExtra("alert",0)
        Log.i("time","$alertId  id in reciver")
        val notificationManager = NotificationManagerCompat.from(context!!)
        notificationManager.cancel(alertId!!)
    }*/





}