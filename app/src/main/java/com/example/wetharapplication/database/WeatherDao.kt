package com.example.wetharapplication.database

import androidx.room.*
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

}