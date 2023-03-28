package com.example.wetharapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.wetharapplication.model.MyResponse


@Database(entities = arrayOf(MyResponse::class) , version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getWeatherDao() : WeatherDao
    companion object {
        @Volatile
        private var INSTANCE :AppDatabase?=null

        fun getInstance (ctx: Context):AppDatabase{
            return INSTANCE?: synchronized(this){
                val instance  = Room.databaseBuilder(
                    ctx.applicationContext,AppDatabase::class.java,"Weather_DataBase3"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }


}