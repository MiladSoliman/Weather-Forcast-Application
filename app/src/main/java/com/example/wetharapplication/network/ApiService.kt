package com.example.wetharapplication.network

import com.example.wetharapplication.model.MyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
  @GET("onecall")
    suspend fun getData(@Query("lat") lat:Double
                        , @Query("lon") lon: Double,
                        @Query("units") units :String = "metric",
                         @Query("lang") lang: String = "ar",
                        @Query("appid") appid:String = "04140387a0c9b9535e981afe74e9ac2a" ): Response<MyResponse>
}