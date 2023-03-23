package com.example.wetharapplication.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wetharapplication.model.MyResponse
import com.example.wetharapplication.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel (private val  myrepo: RepositoryInterface) : ViewModel()  {
    private var myResponse : MutableLiveData<MyResponse> = MutableLiveData<MyResponse>()
    var _myResponse : LiveData<MyResponse> = myResponse

    init {
        getResonse()
    }

    private fun getResonse() {
        viewModelScope.launch(Dispatchers.IO) {
            myResponse.postValue(myrepo.getDataFromApi(31.0 , 30.0))
        }
    }
}