package com.example.wetharapplication.model

import com.example.wetharapplication.database.LocalSource
import com.example.wetharapplication.network.RemoteSource

interface RepositoryInterface : RemoteSource , LocalSource{
}