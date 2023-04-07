package com.example.wetharapplication.database

import androidx.room.*
import com.example.wetharapplication.alert.model.MyAlert
import com.example.wetharapplication.model.MyResponse
import kotlinx.coroutines.flow.Flow


@Dao
interface WeatherDao {
    @Query("SELECT * From Weather")
    fun getFavWeathers (): Flow<List<MyResponse>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(myResponse: MyResponse)
    @Delete
    suspend fun deleteCountry(myResponse: MyResponse)
    @Query("SELECT * FROM Weather WHERE lat LIKE :latitude AND lon LIKE :longitude")
    fun getSelectedWeather(latitude:Double ,longitude:Double):Flow<MyResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(myAlert: MyAlert)

    @Delete
    suspend fun deletAlert(myAlert: MyAlert)

    @Query("SELECT * From Alerts")
    fun getAlerts (): Flow<List<MyAlert>>




}