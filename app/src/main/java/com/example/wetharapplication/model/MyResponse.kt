package com.example.wetharapplication.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "Weather" , primaryKeys = ["lat", "lon"])
data class MyResponse  (
    val lat: Double,
    val lon: Double,
    val timezone: String?,
    val timezoneOffset: Long,
    @Embedded
    val current: Current?,
    val hourly: List<Current>,
    val daily: List<Daily>,
) : Serializable{
    constructor():this(0.0 , 0.0 , null ,0L ,null, listOf() , listOf()  )
}

data class Current (
    val dt: Long,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: Double,
    val feelsLike: Double,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val uvi: Double,
    val clouds: Long,
    val visibility: Long,
    val windSpeed: Double,
    val windDeg: Long,
    val windGust: Double,
    val weather: List<Weather>,
){
    constructor():this(0L , 0L , 0L ,0.0 ,0.0, 0L,0L,0.0,0.0,0L,0L,0.0,0L,0.0, listOf() )
}


data class Weather (
    val id: Long,
    val description: String?,
    val icon: String?
){
    constructor():this(0L,null,null)
}



data class Daily (
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val moonrise: Long,
    val moonset: Long,
    val moonPhase: Double,
    val temp: Temp,
    val feelsLike: FeelsLike,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val windSpeed: Double,
    val windDeg: Long,
    val windGust: Double,
    val weather: List<Weather>,
    val clouds: Long,
    val pop: Double,
    val uvi: Double,
    val rain: Double? = null
)

data class FeelsLike (
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
){
    constructor():this(0.0,0.0,0.0,0.0)
}

data class Temp (
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
){
    constructor():this(0.0,0.0,0.0,0.0,0.0,0.0)

}