package com.example.wetharapplication.favorite.viewmodel.detilas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wetharapplication.model.ApiState
import com.example.wetharapplication.model.MyResponse
import com.example.wetharapplication.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavDetailsViewModel (private var myRepo: RepositoryInterface) : ViewModel() {
   /* private var DetilalsOFFavWeathers : MutableLiveData<MyResponse> = MutableLiveData<MyResponse>()
    var _FavWeathers : LiveData<MyResponse> = DetilalsOFFavWeathers*/

    private var detailsOfFavWeather = MutableStateFlow<ApiState>(ApiState.Loading)
    var _detailsOfFavWeather : StateFlow<ApiState> = detailsOfFavWeather


    fun getLocalWeather(lat:Double , long:Double ){
        viewModelScope.launch(Dispatchers.IO) {
           myRepo.getSelectedWeather(lat,long).collect{
               detailsOfFavWeather.value = ApiState.Success(it)
           }
        }
    }

    fun getWeatherFromApi(lat: Double,long: Double , unites:String , language :String){
        viewModelScope.launch {
            //DetilalsOFFavWeathers.postValue(myRepo.getDataFromApi(lat,long,unites,language))
            myRepo.getDataFromApi(lat , long , unites,language)
                .catch {  e -> detailsOfFavWeather.value = ApiState.Failure(e) }
                .collect{data-> detailsOfFavWeather.value =ApiState.Success(data)}
        }
        }



    fun UpdateWeather(lat:Double,long:Double ){
        viewModelScope.launch  {
            myRepo.getDataFromApi(lat,long,"metric","ar").catch { e->ApiState.Failure(e) }
                .collect{
                    myRepo.insertCountry(it)
                }
        }
    }

}