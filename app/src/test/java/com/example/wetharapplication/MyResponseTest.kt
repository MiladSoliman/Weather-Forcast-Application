package com.example.wetharapplication.favorite.viewmodel.myFav

import com.example.wetharapplication.model.*

fun getMyTestResponse1() : MyResponse{
    var weather1 = Weather(1L,"Good Weather","10d")
    var weather2 = Weather(2L,"greate Weather","15d")
    var temp = Temp(5.5,5.5,5.5,5.5,5.5,5.5)
    var feelsLike = FeelsLike(5.5,5.5,5.5,5.5)
    var daily = Daily(10L,10L,10L,10L,10L,12.5, temp ,feelsLike,10L,10L,5.5,5.5,10L,5.5, listOf(weather1,weather2),10L,5.5,5.5,5.5)
    var alerts = Alerts("Null","Null",1,2,"the weather is good", arrayListOf("one","two"))
    var current = Current(10L,10L,10L,5.5,5.5,10L,10L,5.5,5.5,10L,10L,5.5,10L,5.5, listOf(weather1,weather2))

    var myResponse = MyResponse(31.0,31.0,"Africa,Cairo",10L,current, listOf(current), listOf(daily),
        listOf(alerts)
    )
    return myResponse
}

fun getMyTestResponse2():MyResponse{
    var weather1 = Weather(1L,"Good Weather","10d")
    var weather2 = Weather(2L,"greate Weather","15d")
    var temp = Temp(5.5,5.5,5.5,5.5,5.5,5.5)
    var feelsLike = FeelsLike(5.5,5.5,5.5,5.5)
    var daily = Daily(10L,10L,10L,10L,10L,12.5, temp ,feelsLike,10L,10L,5.5,5.5,10L,5.5, listOf(weather1,weather2),10L,5.5,5.5,5.5)
    var alerts = Alerts("Null","Null",1,2,"the weather is good", arrayListOf("one","two"))
    var current = Current(10L,10L,10L,5.5,5.5,10L,10L,5.5,5.5,10L,10L,5.5,10L,5.5, listOf(weather1,weather2))

    var myResponse2 = MyResponse(30.0,30.0,"Africa,Cairo",10L,current, listOf(current), listOf(daily),
        listOf(alerts)
    )
    return myResponse2
}
