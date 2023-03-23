package com.example.wetharapplication.home.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wetharapplication.model.Location
import com.example.wetharapplication.model.MyResponse
import com.example.wetharapplication.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel (private val  myrepo: RepositoryInterface ,  private val context: Context) : ViewModel()  {
    private var myResponse : MutableLiveData<MyResponse> = MutableLiveData<MyResponse>()
    var _myResponse : LiveData<MyResponse> = myResponse

  /* init {
        location.getLastLocation()
        getResonse()
    }*/

    private fun getResonse(lat:Double , long:Double) {
        viewModelScope.launch(Dispatchers.IO) {
            myResponse.postValue(myrepo.getDataFromApi(lat , long))
        }
    }

    fun getLocation(){
        var location = Location(context)
        location.getLastLocation()
        location.myLocation.observe(context as LifecycleOwner){
            Log.i("my locatio" , "" +it.get(0) + it.get(1))
            getResonse(it.get(0) , it.get(1))
                  }
    }
}