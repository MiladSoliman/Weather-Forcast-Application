package com.example.wetharapplication.favorite.viewmodel.detilas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wetharapplication.model.MyResponse
import com.example.wetharapplication.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavDetailsViewModel (private var myRepo: RepositoryInterface) : ViewModel() {
    private var DetilalsOFFavWeathers : MutableLiveData<MyResponse> = MutableLiveData<MyResponse>()
    var _FavWeathers : LiveData<MyResponse> = DetilalsOFFavWeathers


    fun getLocalWeather(lat:Double , long:Double ){
        viewModelScope.launch(Dispatchers.IO) {
           myRepo.getSelectedWeather(lat,long).collect{
               DetilalsOFFavWeathers.postValue(it)
           }
        }
    }

    fun getWeatherFromApi(lat: Double,long: Double){
        viewModelScope.launch {
            DetilalsOFFavWeathers.postValue(myRepo.getDataFromApi(lat,long))
        }
    }


    fun UpdateWeather(lat:Double,long:Double){
        viewModelScope.launch(Dispatchers.IO) {
            myRepo.insertCountry(myRepo.getDataFromApi(lat,long))
        }
    }

}