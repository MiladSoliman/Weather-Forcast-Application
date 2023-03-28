package com.example.wetharapplication.favorite.viewmodel.detilas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wetharapplication.favorite.viewmodel.myFav.FavVeiwModel
import com.example.wetharapplication.model.RepositoryInterface

class FavDetailsModelFactory (private val myrepo: RepositoryInterface) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(FavDetailsViewModel ::class.java)){
            FavDetailsViewModel (myrepo) as T
        }else{
            throw java.lang.IllegalArgumentException("FavDetailsViewModel  Class not Found")
        }
    }
}