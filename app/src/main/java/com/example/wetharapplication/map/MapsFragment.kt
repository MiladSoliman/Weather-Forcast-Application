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
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.wetharapplication.R
import com.example.wetharapplication.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import androidx.navigation.fragment.navArgs
import com.example.wetharapplication.database.ConcreteLocalSource
import com.example.wetharapplication.favorite.viewmodel.myFav.FavVeiwModel
import com.example.wetharapplication.favorite.viewmodel.myFav.FavViewModelFactory
import com.example.wetharapplication.model.Repository
import com.example.wetharapplication.network.WeatherClient
import java.util.*

class MapsFragment : Fragment() {
    lateinit var binding: FragmentMapsBinding
    lateinit var map: GoogleMap
    var lat: Double = 31.0
    var lon: Double = 30.0
    lateinit var favFactory: FavViewModelFactory
    lateinit var favModel: FavVeiwModel
    val args:MapsFragmentArgs by navArgs()

  //  lateinit var key: String
    private val callback = OnMapReadyCallback { googleMap ->
        searchOnMap()
        map = googleMap
        googleMap.setOnMapClickListener { location ->
            map.clear()
            googleMap.addMarker(MarkerOptions().position(location))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
            lat = location.latitude
            lon = location.latitude
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

    private fun searchOnMap() {
        binding.searchFav.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event.action == KeyEvent.ACTION_DOWN ||
                event.action == KeyEvent.KEYCODE_ENTER
            ) {
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
        var geoCoder: Geocoder = Geocoder(requireContext())
        var list: List<Address> = listOf()
        try {
            list = geoCoder.getFromLocationName(searchLocation, 1) as List<Address>
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (list.size > 0) {
            var adress: Address = list.get(0)
          //  var location: String = adress.adminArea
            lat = adress.latitude
            lon = adress.longitude
            goToLatAndLonf(lat, lon, 17f)
        }
    }

    private fun goToLatAndLonf(latitude: Double, longitude: Double, fl: Float) {
        var latLng = LatLng(latitude, longitude)
        var cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17f)
        map.addMarker(MarkerOptions().position(latLng))
        map.animateCamera(cameraUpdate)
        binding.FabSaveFavLocation.visibility = View.VISIBLE


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


        binding.FabSaveFavLocation.setOnClickListener {
           if (args.from.equals("home")){
                activity?.getSharedPreferences("My Shared", Context.MODE_PRIVATE)?.edit()
                   ?.apply {
                       putBoolean("Map",false)
                       putFloat("lat",lat.toFloat())
                       putFloat("long",lon.toFloat())
                       apply()
                       Log.i("my location",""+lat + lon )
                   }
               Navigation.findNavController(view).navigate(R.id.From_Map_To_Home)
           }else{
               favFactory =
                   FavViewModelFactory (Repository.getInstance(WeatherClient.getInstance(), ConcreteLocalSource.getInstance(requireContext())) )
               favModel =
                   ViewModelProvider(requireActivity(),  favFactory).get(FavVeiwModel::class.java)
               Log.i("fav","" +lat+lon)
               val geocoder = Geocoder(requireContext(), Locale.getDefault())
               var addressList:List<Address> = geocoder.getFromLocation(lat,lon,1) as List<Address>
               if (addressList.size!=0 && !addressList.isEmpty() ) {
                   favModel.insertWeather(lat, lon,)
                   Navigation.findNavController(view).navigate(R.id.FromMapToFav)
               }else{
                   Toast.makeText(requireContext(),"Please Choose Valid Location",Toast.LENGTH_SHORT).show()
               }
           }
        }


    }
}