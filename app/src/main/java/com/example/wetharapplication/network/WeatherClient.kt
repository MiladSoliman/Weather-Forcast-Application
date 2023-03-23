package com.example.wetharapplication.network

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

    override suspend fun getDataFromApi(lat: Double, lon: Double): MyResponse {
        val response =  MyApi.retrofirService.getData(lat,lon)
           if (response.isSuccessful){
                myResponse = response.body()!!
           }
        return myResponse
    }
}