package com.example.wetharapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.wetharapplication.alert.model.MyAlert
import com.example.wetharapplication.model.MyResponse


@Database(entities = arrayOf(MyResponse::class , MyAlert::class) , version = 17)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getWeatherDao() : WeatherDao
    companion object {
        @Volatile
        private var INSTANCE :AppDatabase?=null

        fun getInstance (ctx: Context):AppDatabase{
            return INSTANCE?: synchronized(this){
                val instance  = Room.databaseBuilder(
                    ctx.applicationContext,AppDatabase::class.java,"Weathers_DataBase18"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }


}