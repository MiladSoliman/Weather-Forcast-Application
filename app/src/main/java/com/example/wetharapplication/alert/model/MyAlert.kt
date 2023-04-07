package com.example.wetharapplication.alert.model

import androidx.room.Entity

@Entity(tableName = "Alerts" ,primaryKeys = ["startDay", "startTime"])
data class MyAlert(
    var startDay:String,
    var endDay:String,
    var startTime:String,
    var endTime:String,
    var notifcation:String,
    var alartId:Int
)
