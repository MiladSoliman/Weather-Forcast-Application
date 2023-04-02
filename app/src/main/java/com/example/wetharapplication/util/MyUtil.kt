package com.example.wetharapplication.util

import java.text.SimpleDateFormat
import java.util.*

class MyUtil {
    fun convertDataAndTime(date:Long?):String{
        var simpleDate = SimpleDateFormat("dd/M/yyyy - hh:mm:a ")
        var currentDate = simpleDate.format(date?.times(1000L)).toString()
        return currentDate
    }


    fun getDegreeUnit(unit:String , language:String):String{
        var char = "  ْ K"
        if (language.equals("en")){
            when(unit){
                "standard" -> {char = "ْ K"}
                "metric" -> { char = "ْ C" }
                "imperial" -> {char = "ْ F" }
            }
        }else{
            when(unit){
                "standard" -> {char = "  ك  ْ "}
                "metric" -> { char =  "  س ْ " }
                "imperial" -> {char = "  ف ْ " }
            }
        }

        return char
    }

    fun getWindSpeedUnit(unit: String , language: String):String{
        var char = "m/s"
        if (language.equals("en")){
            when(unit){
                "standard" -> {char = "m/s"}
                "metric" -> { char = "m/s" }
                "imperial" -> {char = "M/h" }
            }
        }else{
            when(unit){
                "standard" -> {char = "متر/ث"}
                "metric" -> { char =   "متر/ث"}
                "imperial" -> {char = "ميل/س" }
            }
        }
        return char

    }


    fun ConvertToDay(dt:Long): String{
        var date= Date(dt*1000L)
        var sdf= SimpleDateFormat("d")
        sdf.timeZone= TimeZone.getDefault()
        var formatedData=sdf.format(date)
        var intDay=formatedData.toInt()
        var calendar= Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH,intDay)
        var format=SimpleDateFormat("EEEE")
        var day=format.format(calendar.time)
        return day
    }

    fun convertToHour(hour:Long) : String{
        var date= Date(hour*1000L)
        var sdf= SimpleDateFormat("hh:mm a")
        sdf.timeZone=TimeZone.getDefault()
        var formatedData=sdf.format(date)
        return formatedData
    }
 }