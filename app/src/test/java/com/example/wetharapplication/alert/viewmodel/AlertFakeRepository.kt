package com.example.wetharapplication.alert.viewmodel

import com.example.wetharapplication.alert.model.MyAlert
import com.example.wetharapplication.model.MyResponse
import com.example.wetharapplication.model.RepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AlertFakeRepository : RepositoryInterface {
    private val alerts : MutableList<MyAlert> = mutableListOf()

    override suspend fun getDataFromApi(
        lat: Double,
        lon: Double,
        unite: String,
        language: String
    ): Flow<MyResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun insertCountry(myResponse: MyResponse) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCountry(myResponse: MyResponse) {
        TODO("Not yet implemented")
    }

    override suspend fun getStoredCountries(): Flow<List<MyResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSelectedWeather(lat: Double, lon: Double): Flow<MyResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(myAlert: MyAlert) {
       alerts.add(myAlert)
    }

    override suspend fun deletAlert(myAlert: MyAlert) {
        alerts.remove(myAlert)
    }

    override suspend fun getAlerts(): Flow<List<MyAlert>> {
        val alertflow = flow {
            val myAlertList = alerts.toList()
            emit(myAlertList)
        }
        return alertflow
    }
}