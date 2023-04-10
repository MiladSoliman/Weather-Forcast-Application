package com.example.wetharapplication.favorite.model.details

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.wetharapplication.R
import com.example.wetharapplication.database.ConcreteLocalSource
import com.example.wetharapplication.databinding.FavItemBinding
import com.example.wetharapplication.databinding.FragmentFavDetailsBinding
import com.example.wetharapplication.favorite.viewmodel.detilas.FavDetailsModelFactory
import com.example.wetharapplication.favorite.viewmodel.detilas.FavDetailsViewModel
import com.example.wetharapplication.home.model.DailyAdapter
import com.example.wetharapplication.home.model.HourlyAdapter
import com.example.wetharapplication.home.viewmodel.HomeViewModel
import com.example.wetharapplication.home.viewmodel.HomeViewModelFactory
import com.example.wetharapplication.map.MapsFragmentArgs
import com.example.wetharapplication.model.*
import com.example.wetharapplication.network.InternetCheck
import com.example.wetharapplication.network.WeatherClient
import com.example.wetharapplication.util.MyUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FavDetailsFragment : Fragment() {
    lateinit var binding: FragmentFavDetailsBinding
    lateinit var detailsFactory: FavDetailsModelFactory
    lateinit var detilsModel: FavDetailsViewModel
    lateinit var hoursList: List<Current>
    lateinit var daysList: List<Daily>
    var latitude = 0.0
    var longitude = 0.0
    val args: FavDetailsFragmentArgs by navArgs()
    lateinit var set: SharedPreferences
    lateinit var favlanguage: String
    lateinit var favunites: String
    lateinit var response: MyResponse
    var myUtil = MyUtil()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.title =
            requireActivity().getString(R.string.favouriteFragment)
        latitude = args.lat.toDouble()
        longitude = args.lon.toDouble()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var context: Context = requireContext()
        detailsFactory = FavDetailsModelFactory(Repository.getInstance(WeatherClient.getInstance(), ConcreteLocalSource.getInstance(context))
        )
        detilsModel = ViewModelProvider(requireActivity(), detailsFactory).get(FavDetailsViewModel::class.java)
        set = activity?.getSharedPreferences("My Shared", Context.MODE_PRIVATE)!!
        favlanguage = set?.getString("language", "en")!!
        favunites = set?.getString("units", "standard")!!

        if (InternetCheck.getConnectivity(context) == true) {
            detilsModel.getWeatherFromApi(latitude, longitude, favunites, favlanguage)
            //update Weather
            detilsModel.UpdateWeather(latitude, longitude)
        } else {
            var lat2 = String.format("%.4f", latitude).toDouble()
            var long2 = String.format("%.4f", longitude).toDouble()
            detilsModel.getLocalWeather(lat2, long2)
            val snakbar = Snackbar.make(
                view,
                context.resources.getString(R.string.snakbar_msg),
                Snackbar.LENGTH_LONG
            ).setAction("Action", null)
            snakbar.show()
        }


        lifecycleScope.launch {
            detilsModel._detailsOfFavWeather.collectLatest { result ->
                when (result) {
                    is ApiState.Loading -> {
                        binding.favprogressBar.visibility = View.VISIBLE
                        hideData()
                    }
                    is ApiState.Success -> {
                        binding.favprogressBar.visibility = View.GONE
                        response = result.data
                        showData()
                        setData()
                    }
                    else -> {
                        binding.favprogressBar.visibility = View.GONE
                        val snakbar = Snackbar.make(requireView(), "There is an Erorr , Please check your Network", Snackbar.LENGTH_LONG).setAction("Action", null)
                        snakbar.show()
                    }
                }

            }
        }
    }

    private fun hideData(){
        binding.tvFavCity.visibility = View.GONE
        binding.tvFavDate.visibility = View.GONE
        binding.tvFavDescription.visibility = View.GONE
        binding.degreedCard.visibility = View.GONE
        binding.cardView7.visibility = View.GONE
        binding.cardView9.visibility = View.GONE
        binding.cardView10.visibility = View.GONE
        binding.cardView11.visibility = View.GONE
        binding.cardView12.visibility = View.GONE
        binding.cardView13.visibility = View.GONE
        binding.favHourlyRecyclerview.visibility = View.GONE
        binding.favDailyRecyclerview.visibility=View.GONE
    }

    private fun showData(){
        binding.tvFavCity.visibility = View.VISIBLE
        binding.tvFavDate.visibility = View.VISIBLE
        binding.tvFavDescription.visibility = View.VISIBLE
        binding.degreedCard.visibility = View.VISIBLE
        binding.cardView7.visibility = View.VISIBLE
        binding.cardView9.visibility = View.VISIBLE
        binding.cardView10.visibility = View.VISIBLE
        binding.cardView11.visibility = View.VISIBLE
        binding.cardView12.visibility = View.VISIBLE
        binding.cardView13.visibility = View.VISIBLE
        binding.favHourlyRecyclerview.visibility = View.VISIBLE
        binding.favDailyRecyclerview.visibility=View.VISIBLE
    }

    private fun setData(){
        try{
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            var addressList:List<Address> = geocoder.getFromLocation(response.lat,response.lon,1) as List<Address>
            if (addressList.size != 0){
                var area = addressList.get(0).countryName
                var country = addressList.get(0).adminArea
                binding.tvFavCity.text = country +" , "+ area
            }else{
                binding.tvFavCity.text = "Welcome Home"
            }
        }catch (e : IOException){
            binding.tvFavCity.text = response.timezone
        }catch (e:RemoteException){
            binding.tvFavCity.text = response.timezone
        }
        binding.tvFavDescription.text = response.current?.weather?.get(0)?.description
        var myCurrentDate = myUtil.convertDataAndTime(response.current?.dt)
        binding.tvFavDate.text = myCurrentDate
        var temp = String.format("%.0f", response.current?.temp)
        var char = myUtil.getDegreeUnit(favunites,favlanguage)
        binding.tvFavHomedegree.text = temp + char
        binding.tvFavEditHumidity.text = response.current?.humidity.toString()+ " " + "%"
        binding.tvFavEditCloud.text = response.current?.clouds.toString()
        binding.tvFavEditIsabilty.text = response.current?.visibility.toString() + " " + "m"
        binding.tvFavEditPressur.text =
            response.current?.pressure.toString() + " " + "hpa"
        binding.tvFavEditUv.text = response.current?.uvi.toString()
        var wChar = myUtil.getWindSpeedUnit(favunites,favlanguage)
        binding.tvFavEditWindspeed.text = response.current?.windSpeed.toString()+wChar
        binding.favImage.setImageResource(myUtil.cheangeIcon(response.current?.weather?.get(0)?.icon))
        hoursList = response.hourly
        Log.i("Ehab", response.hourly.toString())
        binding.favHourlyRecyclerview.apply {
            adapter = FavHourlyAdapter(hoursList, context)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        daysList = response.daily
        binding.favDailyRecyclerview.apply {
            adapter = FavDailyAdapter(daysList, context)
            layoutManager = LinearLayoutManager(context)
        }
    }
}

















