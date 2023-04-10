package com.example.wetharapplication.alert.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wetharapplication.MainDispatcherRule
import com.example.wetharapplication.alert.model.MyAlert
import com.example.wetharapplication.favorite.viewmodel.myFav.FakeReposatory
import com.example.wetharapplication.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlertViewModelTest{
    @get:Rule
    var instanceExecuteRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    lateinit var alertViewModel: AlertViewModel
    lateinit var repo :AlertFakeRepository
    lateinit var alert1 : MyAlert
    lateinit var alert2 :MyAlert

    @Before
    fun setup(){
       // given -> create objects
       repo = AlertFakeRepository()
        alertViewModel = AlertViewModel(repo)
        alert1 = MyAlert("saturday","monday","08:50","09:50","notification",1)
        alert2 = MyAlert("sunday","thursday","8.00","9.00","alaram",2)

    }

    @Test
    fun insertAlert_AndCheckTheListisNotEmpty()= runBlockingTest {
       // when->call insert Method
        alertViewModel.insertAlert(alert1)
        // then -> check the result
       var value = alertViewModel._alerts.getOrAwaitValue {  }
        assertThat(value,not(nullValue()))

    }

    @Test
    fun deleteAlert_CheckTheListIsEmpty() = runBlockingTest {
        // when-> Call insert and delete Methods
        alertViewModel.insertAlert(alert1)
        alertViewModel.deletAlert(alert1)

       var value = alertViewModel._alerts.getOrAwaitValue {  }
       assertThat(value,`is`(emptyList()))

    }

    @Test
    fun getAlerts_CheckIsListisNotEmpty()=runBlockingTest {
       // when -> Call insert methods
        alertViewModel.insertAlert(alert1)
        alertViewModel.insertAlert(alert2)
        //then - > check of getAlerts Method
        alertViewModel.getAlerts()
       var value = alertViewModel._alerts.getOrAwaitValue { }
        assertThat(value,`is`(not(emptyList())))
    }




}