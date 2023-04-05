package com.example.wetharapplication.alert

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.wetharapplication.database.ConcreteLocalSource
import com.example.wetharapplication.model.Repository
import com.example.wetharapplication.network.WeatherClient
import androidx.core.content.ContentProviderCompat.requireContext

class AlertViewModel : ViewModel() {
  // var repo : Repository = Repository.getInstance(WeatherClient.getInstance(),ConcreteLocalSource.getInstance(req))

}