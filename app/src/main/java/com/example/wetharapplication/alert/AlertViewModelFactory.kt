package com.example.wetharapplication.alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wetharapplication.home.viewmodel.HomeViewModel
import com.example.wetharapplication.model.RepositoryInterface

class AlertViewModelFactory (private val myrepo: RepositoryInterface) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AlertViewModel::class.java)){
            AlertViewModel(myrepo) as T
        }else{
            throw java.lang.IllegalArgumentException(" AlertViewModel Class not Found")
        }
    }
}