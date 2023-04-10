package com.example.wetharapplication.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wetharapplication.MainDispatcherRule
import com.example.wetharapplication.favorite.viewmodel.myFav.getMyTestResponse1
import com.example.wetharapplication.favorite.viewmodel.myFav.getMyTestResponse2
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryTest {
    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var remotSource:FakeReposatoryInterface
    lateinit var localSource: FakeReposatoryInterface
    lateinit var repo:Repository
    lateinit var response1: MyResponse
    lateinit var response2: MyResponse
    @Before
    fun setup(){
        // given -> create Objects
       remotSource = FakeReposatoryInterface()
       localSource = FakeReposatoryInterface()
       repo = Repository.getInstance(remotSource,localSource)
        response1 = getMyTestResponse1()
        response2 = getMyTestResponse2()
    }

    @Test
    fun getDataFromApi_CheckTheReturnAsExeprcted() = runBlockingTest {
    // when -> call the method that call api
      var result = repo.getDataFromApi(31.0,31.0,"metrice","en")
     // then -> check the result
      var expexted = flowOf(response1)
      assertEquals(expexted.toList(),result.toList())
    }


    @Test
    fun insertCountry_andCheckTheListIsNotEmpty()= runBlockingTest{
        // when-> call the insert method in the repo
        repo.insertCountry(response1)
        // then -> check the List is not empty
       var reslut =  repo.getStoredCountries()
       assertThat(reslut.toList(),`is`(not(nullValue())))

    }

    @Test
    fun deleteCountry_CheckTheListIsEmpty()= runBlockingTest {
      // when-> call repo method
    //insert country
        repo.insertCountry(response1)
     // delete the country
        repo.deleteCountry(response1)
      //then -> check the reslu
       repo.getStoredCountries().collect{
          assertThat(it,`is`(emptyList()))
      }
    }







   @Test
   fun getSelectedWeather_CheckTheResult() = runBlockingTest {
       repo.insertCountry(response1)
       repo.insertCountry(response2)
      // when -> call the method get the weather by lat and long
       var result = repo.getSelectedWeather(31.0,31.0)
       // then -> check the result
       var expexted = flowOf(response1)
       assertEquals(expexted.toList(),result.toList())

   }




}