package com.example.wetharapplication.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.wetharapplication.alert.model.MyAlert
import com.example.wetharapplication.favorite.viewmodel.myFav.getMyTestResponse1
import com.example.wetharapplication.favorite.viewmodel.myFav.getMyTestResponse2
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class ConcreteLocalSourceTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var localDataSource: ConcreteLocalSource

    @Before
    fun setup(){
      localDataSource = ConcreteLocalSource.getInstance(ApplicationProvider.getApplicationContext())
    }



   @Test
   fun insertCountry_AndRetriveItByLatAndLon_CheckIsTheCorrectData()= runBlocking {
       //given-> create object of my response
       var response = getMyTestResponse1()
       // when-> call insert method and retrive it by lon and long
       localDataSource.insertCountry(response)
       //then -> check the result
       var result = localDataSource.getSelectedWeather(response.lat,response.lon).getOrAwaitValue {  }
       assertThat(result,`is`(response))
       assertThat(result.timezone , `is` (response.timezone))
       assertThat(result.timezoneOffset , `is` (response.timezoneOffset))
       // delete the test response from data base
       localDataSource.deleteCountry(response)
   }


  @Test
  fun insertAlert_DeletethisItem_checkisTheListHaveTheAlertOrNot()= runBlockingTest {
      //given-> create object of my alert
      var alert = MyAlert("start","end","from","to","alarm",1)
      //when -> call insert method and delete it
      localDataSource.insertAlert(alert)
      localDataSource.deletAlert(alert)
      //then -> check the list is contain this alert or not
      var result = localDataSource.getAlerts().getOrAwaitValue {  }
      Assertions.assertThat(result).doesNotContain(alert)
  }



}