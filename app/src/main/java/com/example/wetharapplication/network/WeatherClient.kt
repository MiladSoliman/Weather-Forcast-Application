package com.example.wetharapplication.network

import android.annotation.SuppressLint
import com.example.wetharapplication.model.MyResponse

class WeatherClient : RemoteSource {
    lateinit var myResponse :MyResponse

    companion object {
        @Volatile
        private var INSTANCE :WeatherClient?=null

        fun getInstance():WeatherClient{
            return INSTANCE?: synchronized(this){
                val temp =WeatherClient()
                INSTANCE=temp
                temp
            }
        }

    }

    @SuppressLint("SuspiciousIndentation")
    override suspend fun getDataFromApi(lat: Double, lon: Double,  language :String ,unites:String): MyResponse {
        val response =  MyApi.retrofirService.getData(lat,lon , language , unites)
           if (response.isSuccessful){
                myResponse = response.body()!!
           }
        return myResponse
    }
}