package com.example.wetharapplication.alert

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.example.wetharapplication.R
import com.example.wetharapplication.databinding.FragmentAlertDailogBinding
import java.text.SimpleDateFormat


class AlertDailog : DialogFragment()  {
    lateinit var binding:FragmentAlertDailogBinding
    var myAlert: MyAlert = MyAlert("","","","","",0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlertDailogBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.alarmset.setOnClickListener {

            dialog?.cancel()
        }


    binding.FromCard.setOnClickListener{
        val calenderTime = Calendar.getInstance()
        val timePicker = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calenderTime.set(Calendar.HOUR_OF_DAY,hourOfDay)
            calenderTime.set(Calendar.MINUTE,minute)
            calenderTime.timeZone = TimeZone.getDefault()

            updateFromTime(calenderTime)
        }
        val timePickerDialog = TimePickerDialog(requireContext(),timePicker,calenderTime.get(Calendar.HOUR_OF_DAY),calenderTime.get(Calendar.MINUTE),false)

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

        val datePickerDialog = DatePickerDialog(requireContext(),datePicker,calenderDate.get(Calendar.YEAR),calenderDate.get(Calendar.MONTH),calenderDate.get(Calendar.DAY_OF_MONTH))

        val title = "Choose date"
        datePickerDialog.setTitle(title)
       // datePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        datePickerDialog.show()


        }

    binding.ToCard.setOnClickListener {
        val calenderTime = Calendar.getInstance()
        val timePicker = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calenderTime.set(Calendar.HOUR_OF_DAY,hourOfDay)
            calenderTime.set(Calendar.MINUTE,minute)
            calenderTime.timeZone = TimeZone.getDefault()
            updateToTime(calenderTime)
        }
        val timePickerDialog = TimePickerDialog(requireContext(),timePicker,calenderTime.get(Calendar.HOUR_OF_DAY),calenderTime.get(Calendar.MINUTE),false)

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

        val datePickerDialog = DatePickerDialog(requireContext(),datePicker,calenderDate.get(Calendar.YEAR),calenderDate.get(Calendar.MONTH),calenderDate.get(Calendar.DAY_OF_MONTH))

        val title = "Choose date"
        datePickerDialog.setTitle(title)
        // datePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        datePickerDialog.show()

    }

    var selectedButton = binding.notificationRadioGroup.checkedRadioButtonId
    if (selectedButton==R.id.notification_radio){
     myAlert.notifcation = "notification"
    }else if (selectedButton ==R.id.alaram_radio){
        myAlert.notifcation = "alarm"
    }

    }

    private fun updateToDateText(calenderDate: Calendar?) {
        val day = SimpleDateFormat("dd").format(calenderDate?.time)
        val month = SimpleDateFormat("MMM").format(calenderDate?.time)
        val year = SimpleDateFormat("YYYY").format(calenderDate?.time)
        myAlert.endDay = "$day/$month/$year"
        binding.editToDate.text = "$day/$month/$year"

    }

    private fun updateToTime(calenderTime: Calendar?){
        val format = SimpleDateFormat("hh:mm aa")
        val time = format.format(calenderTime?.time)
        myAlert.endTime ="$time"
        binding.editToTime.text = "$time"
    }


    private fun updateFromTime(calenderTime: Calendar?) {
        val format = SimpleDateFormat("hh:mm aa")
        val time = format.format(calenderTime?.time)
        myAlert.startTime = "$time"
        binding.editTime.text = "$time"

    }

    private fun updateFromDateText(calenderDate: Calendar?) {
        val day = SimpleDateFormat("dd").format(calenderDate?.time)
        val month = SimpleDateFormat("MMM").format(calenderDate?.time)
        val year = SimpleDateFormat("YYYY").format(calenderDate?.time)
        myAlert.startDay = "$month-$day-$year"
        binding.editDate.text = "$month-$day-$year"
    }


}