package com.example.wetharapplication.favorite.viewmodel.myFav

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wetharapplication.MainDispatcherRule
import com.example.wetharapplication.getOrAwaitValue
import com.example.wetharapplication.model.MyResponse
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavVeiwModelTest{
    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()


  lateinit var favVeiwModel: FavVeiwModel
  lateinit var repo : FakeReposatory
  lateinit var response1 :MyResponse
  lateinit var response2: MyResponse

  @Before
  fun setup(){
      // given -> create objects of repo and view model
      repo = FakeReposatory()
      favVeiwModel = FavVeiwModel(repo)
      response1 = getMyTestResponse1()
      response2 = getMyTestResponse2()
  }



  @Test
  fun insertWeather_NotNullList() = mainDispatcherRule.runBlockingTest{
      //when -> call insert method
      favVeiwModel.insertWeather(response1.lat,response1.lon)
      //then ->
      val value = favVeiwModel._FavWeathers.getOrAwaitValue {  }
      assertThat(value,not(nullValue()))

  }

  @Test
  fun getFavouriteCountries_NotNullList() =mainDispatcherRule.runBlockingTest {

      favVeiwModel.insertWeather(response1.lat,response1.lon)
      favVeiwModel.insertWeather(response2.lat,response2.lon)


     // when -> call get allCountries
      favVeiwModel.getFavouriteCountries()

     // then-> checks the value
     val value = favVeiwModel._FavWeathers.getOrAwaitValue {  }
      assertThat(value,not(nullValue()))
  }

  @Test
  fun deletCountry_CheckTheCountryIsDelted() = mainDispatcherRule.runBlockingTest {
     favVeiwModel.insertWeather(response1.lat,response1.lon)
      // when -> call view model delete method
      favVeiwModel.deletCountry(response1)
      // then - >checks the result
      val value = favVeiwModel._FavWeathers.getOrAwaitValue {  }
      assertThat(value ,`is`(emptyList()))

  }







}