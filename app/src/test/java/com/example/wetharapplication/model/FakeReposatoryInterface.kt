package com.example.wetharapplication.model

import com.example.wetharapplication.alert.model.MyAlert
import com.example.wetharapplication.database.LocalSource
import com.example.wetharapplication.favorite.viewmodel.myFav.getMyTestResponse1
import com.example.wetharapplication.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeReposatoryInterface : LocalSource , RemoteSource {
    var myStoredResponse : MutableList<MyResponse> = mutableListOf()

    override suspend fun getDataFromApi(lat: Double, lon: Double, unite: String, language: String
    ): Flow<MyResponse> {
        var response1 = getMyTestResponse1()
       var myflwo = flowOf(response1)
        return myflwo
    }



    override suspend fun insertCountry(myResponse: MyResponse) {
        myStoredResponse.add(myResponse)
    }

    override suspend fun deleteCountry(myResponse: MyResponse) {
        myStoredResponse.remove(myResponse)
    }

    override suspend fun getStoredCountries(): Flow<List<MyResponse>> {
        val myflow = flow {
            val myFavList =myStoredResponse.toList()
            emit(myFavList)
        }
        return myflow
    }

    override suspend fun getSelectedWeather(lat: Double, lon: Double): Flow<MyResponse> {
        var mySelectedWeather = MyResponse()
        myStoredResponse.forEach({
            if (it.lat == lat && it.lon==lat){
                mySelectedWeather = it
            }
        })
        return flowOf(mySelectedWeather)
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