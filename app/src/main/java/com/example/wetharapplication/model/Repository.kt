package com.example.wetharapplication.model

import com.example.wetharapplication.alert.MyAlert
import com.example.wetharapplication.database.LocalSource
import com.example.wetharapplication.network.RemoteSource
import kotlinx.coroutines.flow.Flow

class Repository private constructor (var remoteSource: RemoteSource , var localSource: LocalSource)  : RepositoryInterface {

    companion object{
        private var instance:Repository?=null
        fun getInstance(remoteSource: RemoteSource ,localSource: LocalSource):Repository{
            return instance?: synchronized(this){
                val temp = Repository(remoteSource, localSource)
                instance=temp
                temp
            }
        }
    }

    override suspend fun getDataFromApi(lat: Double, lon: Double , unites :String , language:String): Flow<MyResponse> {
        return remoteSource.getDataFromApi(lat , lon ,unites,language )
    }

    override suspend fun insertCountry(myResponse: MyResponse) {
        localSource.insertCountry(myResponse)
    }

    override suspend fun deleteCountry(myResponse: MyResponse) {
        localSource.deleteCountry(myResponse)
    }

    override suspend fun getStoredCountries(): Flow<List<MyResponse>> {
        return localSource.getStoredCountries()
    }

    override suspend fun getSelectedWeather(lat: Double, lon: Double): Flow<MyResponse> {
        return localSource.getSelectedWeather(lat,lon)
    }

    override suspend fun insertAlert(myAlert: MyAlert) {
        localSource.insertAlert(myAlert)
    }

    override suspend fun deletAlert(myAlert: MyAlert) {
       localSource.deletAlert(myAlert)
    }

    override suspend fun getAlerts(): Flow<List<MyAlert>> {
        return localSource.getAlerts()
    }


}