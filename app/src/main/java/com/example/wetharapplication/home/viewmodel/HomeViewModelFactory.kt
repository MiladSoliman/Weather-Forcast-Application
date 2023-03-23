package com.example.wetharapplication.home.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wetharapplication.model.Location
import com.example.wetharapplication.model.RepositoryInterface

class HomeViewModelFactory (private val myrepo: RepositoryInterface , private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(myrepo , context) as T
        }else{
            throw java.lang.IllegalArgumentException(" ViewModel Class not Found")
        }
    }
}
