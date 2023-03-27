package com.example.wetharapplication.model

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.*

const val PERMISSION_ID = 44
class Location ( var context : Context){
  var myLocation :MutableLiveData<List<Double>>  = MutableLiveData<List<Double>>()
  var _myLocation : LiveData<List<Double>> = myLocation


    var mFusedLocationClient: FusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        if(checkPermissions()){
            if(isLocationEnabled()){
                requestNewLocationData()
            }else{
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            }
        }else{
            requestPermissions()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManger: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManger.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }


    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )== PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )== PackageManager.PERMISSION_GRANTED

    }


    private fun requestPermissions(){
        ActivityCompat.requestPermissions(
            context as Activity ,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

  /* override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode== PERMISSION_ID){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                getLastLocation()
            }
        }
    }*/


    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = com.google.android.gms.location.LocationRequest()
        mLocationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(0)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest , mLocationCallBack, Looper.myLooper()
        )
    }


    private val mLocationCallBack : LocationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult?) {
            val mlastLocation : Location = locationResult?.lastLocation!!
            Log.i("MsMS" , ""+mlastLocation.latitude +  mlastLocation.longitude   )
             myLocation.postValue(listOf(mlastLocation.latitude , mlastLocation.longitude ))
            stopLocationUpdates()

        }
    }

    private fun stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallBack)
    }
}


