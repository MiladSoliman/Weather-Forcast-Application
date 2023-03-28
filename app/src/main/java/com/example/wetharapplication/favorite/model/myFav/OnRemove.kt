package com.example.wetharapplication.favorite.model.myFav

import com.example.wetharapplication.model.MyResponse

interface OnRemove {
    fun deleteCountry(response: MyResponse)
}