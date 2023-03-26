package com.example.wetharapplication.map

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.navigation.Navigation
import androidx.transition.Visibility
import com.example.wetharapplication.R
import com.example.wetharapplication.databinding.FragmentMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class MapsFragment : Fragment() {
    lateinit var binding: FragmentMapsBinding
    lateinit var map : GoogleMap
     var lat :Double = 31.0
     var lon :Double = 30.0
    lateinit var key:String


   // var mapFusedLocationClient: FusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(requireContext())
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
       searchOnMap()
       map = googleMap
       googleMap.setOnMapClickListener { location->
           googleMap.addMarker(MarkerOptions().position(location))
           googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
           lat = location.latitude
           lon = location.latitude
          // Log.i("Location" , "" + lat + lon)
           binding.FabSaveFavLocation.visibility = View.VISIBLE
       }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun searchOnMap(){
        binding.searchFav.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH||
                actionId==EditorInfo.IME_ACTION_DONE||
                event.action == KeyEvent.ACTION_DOWN||
                event.action == KeyEvent.KEYCODE_ENTER
            ){
                goToSearchLocation()
                true
            } else {
                binding.searchFav.text.clear()
                binding.searchFav.hint = "Please Enter Your Location"
                false
            }
        }
    }

    private fun goToSearchLocation() {
        var searchLocation = binding.searchFav.text.toString()
        var geoCoder:Geocoder = Geocoder(requireContext())
       var list :List<Address> = listOf()
        try {
            list = geoCoder.getFromLocationName(searchLocation , 1) as List<Address>
        }catch(e:IOException){
            e.printStackTrace()
        }
        if (list.size >0) {
            var adress: Address = list.get(0)
            var location: String = adress.adminArea
            lat  = adress.latitude
            lon = adress.longitude
            goToLatAndLonf(lat , lon , 17f)
        }


    }

    private fun goToLatAndLonf(latitude: Double, longitude: Double, fl: Float) {
         var latLng = LatLng(latitude , longitude)
        var cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng ,17f)
        map.addMarker(MarkerOptions().position(latLng))
        map.animateCamera(cameraUpdate)
        binding.FabSaveFavLocation.visibility = View.VISIBLE
       // Log.i("My Location",""+ latLng.latitude + latLng.longitude )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        var se =  activity?.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        key = se?.getString("Key","Not Found") !!
        se.edit().apply {
            putBoolean("Map",false)
            putFloat("lat",lat.toFloat())
            putFloat("lon",lon.toFloat())
            apply()
        }



        binding.FabSaveFavLocation.setOnClickListener {
           Log.i("Essam" , "" + lat + lon )
           if (key.equals("home")){
               Navigation.findNavController(requireView()).navigate(R.id.From_Map_To_Home)
           }
           else if (key.equals("fav")){

           }

        }
    }



}