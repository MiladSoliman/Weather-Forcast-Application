package com.example.wetharapplication.network

import com.example.wetharapplication.model.MyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
  @GET("onecall")
    suspend fun getData(@Query("lat") lat:Double
                        , @Query("lon") lon: Double,
                         @Query("lang") lang: String = "en",
                        @Query("units") units :String = "metric"
                        ,@Query("appid") appid:String = "416c3f7d60f73a4f8f76c658c93cf3b7" ): Response<MyResponse>
}