package com.example.wetharapplication.database

import android.content.Context
import com.example.wetharapplication.model.MyResponse
import kotlinx.coroutines.flow.Flow

class ConcreteLocalSource private constructor(context: Context) : LocalSource {

    private val dao:WeatherDao by lazy {
        val db:AppDatabase = AppDatabase.getInstance(context)
        db.getWeatherDao()
    }


    companion object{
        @Volatile
        private var INSTANCE :ConcreteLocalSource?=null
        fun getInstance(context: Context): ConcreteLocalSource {
            return INSTANCE?: synchronized(this){
                val temp = ConcreteLocalSource(context)
                INSTANCE=temp
                temp
            }
        }
    }




    override suspend fun insertCountry(myResponse: MyResponse) {
        dao.insertCountry(myResponse)
    }

    override suspend fun deleteCountry(myResponse: MyResponse) {
        dao.deleteCountry(myResponse)
    }

    override suspend fun getStoredCountries(): Flow<List<MyResponse>> {
       return dao.getFavWeathers()
    }

    override suspend fun getSelectedWeather(lat: Double, lon: Double): Flow<MyResponse> {
        return dao.getSelectedWeather(lat,lon)
    }

    override suspend fun getSelectedHOMEWeather(latitude: Double, longitude: Double): Flow<MyResponse> {
        return dao.getSelectedHOMEWeather(latitude,longitude)
    }
}