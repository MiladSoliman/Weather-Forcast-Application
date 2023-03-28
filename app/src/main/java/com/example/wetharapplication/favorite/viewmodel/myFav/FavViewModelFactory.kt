package com.example.wetharapplication.favorite.viewmodel.myFav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wetharapplication.model.RepositoryInterface

class FavViewModelFactory (private val myrepo: RepositoryInterface) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(FavVeiwModel ::class.java)){
            FavVeiwModel (myrepo) as T
        }else{
            throw java.lang.IllegalArgumentException(" FavVeiwModel  Class not Found")
        }
    }
}



