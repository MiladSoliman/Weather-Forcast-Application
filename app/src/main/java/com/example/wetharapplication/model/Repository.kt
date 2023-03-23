package com.example.wetharapplication.model

import com.example.wetharapplication.network.RemoteSource

class Repository private constructor (var remoteSource: RemoteSource)  : RepositoryInterface {

    companion object{
        private var instance:Repository?=null
        fun getInstance(remoteSource: RemoteSource):Repository{
            return instance?: synchronized(this){
                val temp = Repository(remoteSource)
                instance=temp
                temp
            }
        }
    }

    override suspend fun getDataFromApi(lat: Double, lon: Double): MyResponse {
        return remoteSource.getDataFromApi(lat , lon)
    }
}