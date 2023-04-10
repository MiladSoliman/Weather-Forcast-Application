package com.example.wetharapplication.favorite.viewmodel.myFav

import androidx.lifecycle.MutableLiveData
import com.example.wetharapplication.alert.model.MyAlert
import com.example.wetharapplication.model.MyResponse
import com.example.wetharapplication.model.RepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeReposatory : RepositoryInterface {

    private val favoriteCountries : MutableList<MyResponse> = mutableListOf()

    override suspend fun insertCountry(myResponse: MyResponse) {
       favoriteCountries.add(myResponse)
    }

    override suspend fun deleteCountry(myResponse: MyResponse) {
        favoriteCountries.remove(myResponse)
    }

    override suspend fun getStoredCountries(): Flow<List<MyResponse>> {
        val myflow = flow {
            val myFavList = favoriteCountries.toList()
            emit(myFavList)
        }
        return myflow
    }



    override suspend fun getDataFromApi(
        lat: Double,
        lon: Double,
        unite: String,
        language: String
    ): Flow<MyResponse> {
        TODO("Not yet implemented")
    }


    override suspend fun getSelectedWeather(lat: Double, lon: Double): Flow<MyResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(myAlert: MyAlert) {
        TODO("Not yet implemented")
    }

    override suspend fun deletAlert(myAlert: MyAlert) {
        TODO("Not yet implemented")
    }

    override suspend fun getAlerts(): Flow<List<MyAlert>> {
        TODO("Not yet implemented")
    }
}