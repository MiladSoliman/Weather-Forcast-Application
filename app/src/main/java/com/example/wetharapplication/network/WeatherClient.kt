package com.example.wetharapplication.network

import android.annotation.SuppressLint
import com.example.wetharapplication.model.MyResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
    override suspend fun getDataFromApi(lat: Double, lon: Double,  language :String ,unites:String): Flow<MyResponse> {
        val response =  MyApi.retrofirService.getData(lat,lon , language , unites)
           if (response.isSuccessful){
                myResponse = response.body()!!
           }
        var myFlow = flow {
            emit(myResponse)
        }
        return myFlow
    }
}