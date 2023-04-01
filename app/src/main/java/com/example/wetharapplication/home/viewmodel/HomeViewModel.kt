package com.example.wetharapplication.home.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wetharapplication.model.ApiState
import com.example.wetharapplication.model.Location
import com.example.wetharapplication.model.MyResponse
import com.example.wetharapplication.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel (private val  myrepo: RepositoryInterface ) : ViewModel()  {
    //private var myResponse : MutableLiveData<MyResponse> = MutableLiveData<MyResponse>()
   // var _myResponse : LiveData<MyResponse> = myResponse

   private var myResponse = MutableStateFlow<ApiState>(ApiState.Loading)
    var _myResponse : StateFlow<ApiState> = myResponse

   /*init {
        location.getLastLocation()
        getResonse()
    }*/

    fun getWeather(lat:Double , long:Double , unites:String, langage:String) {
        viewModelScope.launch {
           // myResponse.postValue(myrepo.getDataFromApi(lat , long , unites,langage ))
            myrepo.getDataFromApi(lat , long , unites,langage)
                .catch {  e -> myResponse.value = ApiState.Failure(e) }
                .collect{data->myResponse.value =ApiState.Success(data)}
        }
    }



   /* fun getLocation(){
        var location = Location(context)
        location.getLastLocation()
        location.myLocation.observe(context as LifecycleOwner){
            Log.i("my location" , "" +it.get(0) + it.get(1))
            getResonse(it.get(0) , it.get(1))
                  }
    }*/
}