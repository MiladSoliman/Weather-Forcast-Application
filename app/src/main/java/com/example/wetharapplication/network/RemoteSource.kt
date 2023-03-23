package com.example.wetharapplication.network

import com.example.wetharapplication.model.MyResponse

interface RemoteSource {
suspend fun getDataFromApi (lat :Double , lon :Double) :MyResponse
}