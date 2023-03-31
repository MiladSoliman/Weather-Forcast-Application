package com.example.wetharapplication.favorite.viewmodel.myFav

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wetharapplication.model.MyResponse
import com.example.wetharapplication.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavVeiwModel (private var myRepo: RepositoryInterface) : ViewModel(){
    private var FavWeathers : MutableLiveData<List<MyResponse>> = MutableLiveData<List<MyResponse>>()
    var _FavWeathers : LiveData<List<MyResponse>> = FavWeathers

    fun insertWeather(lat:Double , long: Double ){
       viewModelScope.launch(Dispatchers.IO) {
          myRepo.insertCountry(myRepo.getDataFromApi(lat,long,"metric","ar"))
       }
    }



   fun getFavouriteCountries(){
    viewModelScope.launch (Dispatchers.IO){
        myRepo.getStoredCountries().collect(){
            (FavWeathers.postValue(it))
        }
    }
   }

   fun deletCountry(myResponse: MyResponse){
       viewModelScope.launch(Dispatchers.IO) {
           myRepo.deleteCountry(myResponse)
           getFavouriteCountries()
       }
   }




}