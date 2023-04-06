package com.example.wetharapplication.alert

import android.annotation.SuppressLint
import android.app.*
import android.app.PendingIntent.getBroadcast
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import com.example.wetharapplication.R
import com.example.wetharapplication.databinding.FragmentAlertBinding
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.SimpleDateFormat


class AlertFragment : Fragment() {
    lateinit var binding: FragmentAlertBinding
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent
    lateinit var editToTime: TextView
    lateinit var editToDate: TextView
    lateinit var editFromTime: TextView
    lateinit var editFromDate: TextView
    var myAlert: MyAlert = MyAlert("", "", "", "", "", 0)
    lateinit var startDay :String
    lateinit var endDay :String
    lateinit var startTime :String
    lateinit var endTime :String
    lateinit var calenderFromTime : Calendar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            //   activity?.supportFragmentManager?.let { it1 -> alertDailog.show(it1,"Milad") }
            alertDialog()
        }
    }


    @SuppressLint("WrongConstant")
    private fun createNotificationChannel(id:Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Mina Nageh"
            val description = "Cloudy Sky"
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
        intent.putExtra("id",id)
        intent.putExtra("notification",type)
        pendingIntent = PendingIntent.getBroadcast(requireContext(), id, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }


    fun alertDialog() {
        var dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_alert_dailog)
        val window: Window? = dialog.getWindow()
        //  window?.setBackgroundDrawableResource(R.color.transparent);
        window?.setLayout(
            Constraints.LayoutParams.WRAP_CONTENT,
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


        dialog.findViewById<RadioButton>(R.id.notification_radio).setOnClickListener {
            myAlert.notifcation = "notification"
        }
        dialog.findViewById<RadioButton>(R.id.alaram_radio).setOnClickListener {
            myAlert.notifcation = "alarm"
        }



        dialog.findViewById<Button>(R.id.alarmset).setOnClickListener {
            var id =generateUniqueIntValue(startDay,endDay,startTime)
            createNotificationChannel(id)
            var time = calenderFromTime.timeInMillis
            setAlarm(time,id,myAlert.notifcation)
            dialog.dismiss()
        }

    }

    private fun updateToDateText(calenderDate: Calendar?) {
        val day = SimpleDateFormat("dd").format(calenderDate?.time)
        val month = SimpleDateFormat("MMM").format(calenderDate?.time)
        val year = SimpleDateFormat("YYYY").format(calenderDate?.time)
        endDay = "$day/$month/$year"
        myAlert.endDay = "$day/$month/$year"
        editToDate.text = "$day/$month/$year"


    }

    private fun updateToTime(calenderTime: Calendar?) {
        val format = SimpleDateFormat("hh:mm aa")
        val time = format.format(calenderTime?.time)
        endTime = "$time"
        myAlert.endTime = "$time"
        editToTime.text = "$time"
    }


    private fun updateFromTime(calenderTime: Calendar?) {
        val format = SimpleDateFormat("hh:mm aa")
        val time = format.format(calenderTime?.time)
        startTime = "$time"
        myAlert.startTime = "$time"
        editFromTime.text = "$time"

    }

    private fun updateFromDateText(calenderDate: Calendar?) {
        val day = SimpleDateFormat("dd").format(calenderDate?.time)
        val month = SimpleDateFormat("MMM").format(calenderDate?.time)
        val year = SimpleDateFormat("YYYY").format(calenderDate?.time)
        startDay = "$day/$month/$year"
        myAlert.startDay = "$day/$month/$year"
        editFromDate.text = "$day/$month/$year"

    }

    fun showFromTimeAndDatePicker() {
        calenderFromTime = Calendar.getInstance()
        val timePicker = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calenderFromTime .set(Calendar.HOUR_OF_DAY, hourOfDay)
            calenderFromTime .set(Calendar.MINUTE, minute)
            calenderFromTime .timeZone = TimeZone.getDefault()
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

}