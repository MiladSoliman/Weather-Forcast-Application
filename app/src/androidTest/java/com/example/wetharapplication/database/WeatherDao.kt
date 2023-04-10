package com.example.wetharapplication.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.wetharapplication.alert.model.MyAlert
import com.example.wetharapplication.favorite.viewmodel.myFav.getMyTestResponse1
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherDaoTest {

    @get:Rule
    val instance = InstantTaskExecutorRule()
    private lateinit var database : AppDatabase


    @Before
    fun createDataBase(){
        database= Room.inMemoryDatabaseBuilder(getApplicationContext(),AppDatabase::class.java).build()
    }

    @After
    fun closeDataBase(){
        database.close()
    }

    @Test
    fun insertCountry_AndRetriveitByLatAndLong()= runBlocking {
      // given-> create object of my response
        var response = getMyTestResponse1()
    // when - > call insert mettod
        database.getWeatherDao().insertCountry(response)
    // then -> check the result
       var result = database.getWeatherDao().getSelectedWeather(response.lat,response.lon).getOrAwaitValue {}

        assertThat(result,`is`(response))
        assertThat(result.timezone , `is` (response.timezone))
        assertThat(result.timezoneOffset , `is` (response.timezoneOffset))
    }


   @Test
   fun insertCountry_deleteCountry_checksTheReslut() = runBlocking{
      // given -> create object of my response
       var response = getMyTestResponse1()
       // when -> call insert method and delete it
       database.getWeatherDao().insertCountry(response)
       database.getWeatherDao().deleteCountry(response)
       //then -> check is List is empty
      var result =  database.getWeatherDao().getFavWeathers().getOrAwaitValue {  }
      assertThat(result,`is`(emptyList()))

   }

   @Test
   fun insertAlert_andRetriveit_ChecktheReslut()= runBlocking {
       //given-> create object of my alert
       var alert = MyAlert("start","end","from","to","alarm",1)
       // when -> call insert metthod and retriive it
       database.getWeatherDao().insertAlert(alert)
       var result = database.getWeatherDao().getAlerts().getOrAwaitValue { }
       var expectedalert = result[0]
       assertThat(expectedalert , not(nullValue()))
       assertThat(expectedalert.alartId , `is` (1))
       assertThat(expectedalert.startDay , `is` ("start"))
       assertThat(expectedalert.endDay , `is` ("end"))
       assertThat(expectedalert.startTime , `is` ("from"))
       assertThat(expectedalert.endTime , `is` ("to"))
   }


    @Test
    fun insertAlert_CheckTheListIsNotEmpty()= runBlocking {
        //given-> create Object Of Alert
        var alert = MyAlert("start","end","from","to","alarm",1)
        //when-> call insert method and delete it
        database.getWeatherDao().insertAlert(alert)
        database.getWeatherDao().deletAlert(alert)
        //then-> check the list is empty or not
       var result = database.getWeatherDao().getAlerts().getOrAwaitValue {  }
        assertThat(result,`is`(emptyList()))
    }


}







