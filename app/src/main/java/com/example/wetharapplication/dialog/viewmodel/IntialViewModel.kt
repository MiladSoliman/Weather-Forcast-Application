package com.example.wetharapplication.dialog.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wetharapplication.home.viewmodel.HomeViewModel
import com.example.wetharapplication.model.Location
import com.example.wetharapplication.model.RepositoryInterface

class IntialViewModel (var location: Location ) :ViewModel (){

    fun getMyLocation () {
        location.getLastLocation()
    }

    fun observLocation () : LiveData<List<Double>>{
        return location.myLocation
    }
}



class IntialViewModelFactory (private val location: Location) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(IntialViewModel::class.java)){
            IntialViewModel(location) as T
        }else{
            throw java.lang.IllegalArgumentException(" ViewModel Class not Found")
        }
    }
}

