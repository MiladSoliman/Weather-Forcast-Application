package com.example.wetharapplication.network

import com.example.wetharapplication.model.MyResponse
import kotlinx.coroutines.flow.Flow


interface RemoteSource {
suspend fun getDataFromApi (lat :Double , lon :Double , unite:String, language:String) : Flow<MyResponse>
}