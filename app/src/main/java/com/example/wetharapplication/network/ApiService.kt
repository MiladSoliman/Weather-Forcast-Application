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
                        ,@Query("appid") appid:String = "48d4992301c3d1d2f536835ad952332f" ): Response<MyResponse>
}