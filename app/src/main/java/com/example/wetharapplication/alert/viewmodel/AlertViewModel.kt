package com.example.wetharapplication.alert.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wetharapplication.alert.model.MyAlert
import com.example.wetharapplication.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertViewModel (private val  myRepo: RepositoryInterface) : ViewModel() {

    private var alerts : MutableLiveData<List<MyAlert>> = MutableLiveData<List<MyAlert>>()
    var _alerts : LiveData<List<MyAlert>> = alerts

   init {
    getAlerts()
  }


    fun insertAlert(myAlert: MyAlert){
        viewModelScope.launch {
          myRepo.insertAlert(myAlert)
        }
    }



    fun getAlerts(){
        viewModelScope.launch (Dispatchers.IO) {
            myRepo.getAlerts().collect() {
                (alerts.postValue(it))
            }
        }

    }

    fun deletAlert(myAlert: MyAlert){
        viewModelScope.launch(Dispatchers.IO) {
            myRepo.deletAlert(myAlert)
             getAlerts()
        }
    }

}
