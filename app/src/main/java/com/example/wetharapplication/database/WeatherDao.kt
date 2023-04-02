package com.example.wetharapplication.database

import androidx.room.*
import com.example.wetharapplication.model.MyResponse
import kotlinx.coroutines.flow.Flow


@Dao
interface WeatherDao {
    @Query("SELECT * From Weather WHERE STATUES LIKE 'true'")
    fun getFavWeathers (): Flow<List<MyResponse>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(myResponse: MyResponse)
    @Delete
    suspend fun deleteCountry(myResponse: MyResponse)
    @Query("SELECT * FROM Weather WHERE lat LIKE :latitude AND lon LIKE :longitude")
    fun getSelectedWeather(latitude:Double ,longitude:Double):Flow<MyResponse>


    @Query("SELECT * FROM Weather WHERE STATUES Like 'false' AND lat LIKE :latitude AND lon LIKE :longitude")
    fun  getSelectedHOMEWeather(latitude:Double ,longitude:Double):Flow<MyResponse>



}