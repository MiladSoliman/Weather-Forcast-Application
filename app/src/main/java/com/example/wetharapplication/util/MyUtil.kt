package com.example.wetharapplication.util


import com.example.wetharapplication.R
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


    fun cheangeIcon(icon:String?):Int{
        var myImage:Int=-1
         when(icon){
             "01d" -> {myImage = R.drawable.d01}
             "02d"-> {myImage =  R.drawable.d02}
             "03d"-> {myImage =  R.drawable.d03}
             "04d"-> {myImage =  R.drawable.d04}
             "09d"-> {myImage =  R.drawable.d09}
             "10d"-> {myImage =  R.drawable.d10}
             "11d"-> {myImage =  R.drawable.d11}
             "13d"-> {myImage =  R.drawable.d13}
             "50d"-> {myImage =  R.drawable.d50}
             "01n"-> {myImage =  R.drawable.n01}
             "02n"-> {myImage =  R.drawable.n02}
             "03n"-> {myImage =  R.drawable.n03}
             "04n"-> {myImage =  R.drawable.n04}
             "09n"-> {myImage =  R.drawable.n09}
             "10n"-> {myImage =  R.drawable.n10}
             "11n"-> {myImage =  R.drawable.n11}
             "13n"-> {myImage =  R.drawable.n13}
             "50n"-> {myImage =  R.drawable.n50}
         }

  return myImage

    }
 }

