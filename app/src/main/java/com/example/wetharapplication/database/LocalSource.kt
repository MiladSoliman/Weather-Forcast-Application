package com.example.wetharapplication.database

import com.example.wetharapplication.model.MyResponse
import kotlinx.coroutines.flow.Flow

interface LocalSource {
   suspend fun insertCountry (myResponse: MyResponse)
   suspend fun deleteCountry (myResponse: MyResponse)
   suspend fun getStoredCountries () : Flow<List<MyResponse>>

   suspend fun getSelectedWeather(lat:Double,lon:Double) : Flow<MyResponse>

   suspend fun getSelectedHOMEWeather(latitude:Double ,longitude:Double):Flow<MyResponse>

}