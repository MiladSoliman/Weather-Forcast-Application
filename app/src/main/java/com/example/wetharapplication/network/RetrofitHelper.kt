package com.example.wetharapplication.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper{
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    val retrofitInstance = Retrofit.Builder().baseUrl( BASE_URL).addConverterFactory(
        GsonConverterFactory.create()).build()
}


object MyApi {
    val retrofirService: ApiService by lazy {
        RetrofitHelper.retrofitInstance.create(ApiService::class.java)
    }
}