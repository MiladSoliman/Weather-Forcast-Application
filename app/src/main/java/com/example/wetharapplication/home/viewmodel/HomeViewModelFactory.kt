package com.example.wetharapplication.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wetharapplication.model.RepositoryInterface

class HomeViewModelFactory (private val myrepo: RepositoryInterface) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(myrepo) as T
        }else{
            throw java.lang.IllegalArgumentException(" ViewModel Class not Found")
        }
    }
}
