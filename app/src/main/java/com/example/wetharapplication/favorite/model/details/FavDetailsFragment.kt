package com.example.wetharapplication.favorite.model.details

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
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
import com.example.wetharapplication.model.Current
import com.example.wetharapplication.model.Daily
import com.example.wetharapplication.model.MyResponse
import com.example.wetharapplication.model.Repository
import com.example.wetharapplication.network.InternetCheck
import com.example.wetharapplication.network.WeatherClient
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat

class FavDetailsFragment : Fragment() {
    lateinit var binding: FragmentFavDetailsBinding
    lateinit var detailsFactory: FavDetailsModelFactory
    lateinit var detilsModel:FavDetailsViewModel
    lateinit var hoursList: List<Current>
    lateinit var daysList: List<Daily>
    var latitude =0.0
    var longitude =0.0
    val args: FavDetailsFragmentArgs by navArgs()
    lateinit var set:SharedPreferences
    lateinit var favlanguage:String
    lateinit var favunites:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        latitude = args.lat.toDouble()
        longitude = args.lon.toDouble()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =FragmentFavDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         var context:Context = requireContext()
        detailsFactory =FavDetailsModelFactory(Repository.getInstance(WeatherClient.getInstance(), ConcreteLocalSource.getInstance(context)) )
        detilsModel = ViewModelProvider(requireActivity(),  detailsFactory).get(FavDetailsViewModel::class.java)
        set = activity?.getSharedPreferences("My Shared", Context.MODE_PRIVATE)!!
        favlanguage = set?.getString("language","en")!!
        favunites =  set?.getString("units","standard")!!

        if(InternetCheck.getConnectivity(context) == true) {
            detilsModel.getWeatherFromApi(latitude, longitude, favunites,favlanguage)
            //update Weather
            detilsModel.UpdateWeather(latitude,longitude)
        }else{
            var lat2 = String.format("%.4f",latitude).toDouble()
            var long2 = String.format("%.4f",longitude).toDouble()
            detilsModel.getLocalWeather(lat2,long2)
            val snakbar = Snackbar.make(view,context.resources.getString(R.string.snakbar_msg),Snackbar.LENGTH_LONG).setAction("Action",null)
            snakbar.show()
        }

        detilsModel._FavWeathers.observe(requireActivity()){response->
            binding.tvFavCity.text =response.timezone
            binding.tvFavDescription.text = response.current?.weather?.get(0)?.description
            var simpleDate = SimpleDateFormat("dd/M/yyyy - hh:mm:a ")
            var currentDate = simpleDate.format(response.current?.dt?.times(1000L) ?: 0)
            binding.tvFavDate.text = currentDate.toString()
            binding.tvFavHomedegree.text = response.current?.temp.toString()
            binding.tvFavEditHumidity.text = response.current?.humidity.toString()
            binding.tvFavEditCloud.text = response.current?.clouds.toString()
            binding.tvFavEditIsabilty.text = response.current?.visibility.toString()
            binding.tvFavEditPressur.text = response.current?.pressure.toString() + " " + "hpa"
            binding.tvFavEditUv.text = response.current?.uvi.toString()
            binding.tvFavEditWindspeed.text = response.current?.windSpeed.toString()
            Glide.with(context)
                .load("https://openweathermap.org/img/wn/${response.current?.weather?.get(0)?.icon}@2x.png")
                .into(binding.favImage)
            Log.i("dayicon", "" + response.current?.weather?.get(0)?.icon)
            hoursList = response.hourly
            Log.i("Ehab", response.hourly.toString())
            binding.favHourlyRecyclerview.apply {
                adapter = FavHourlyAdapter(hoursList, context)
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            daysList = response.daily
            binding.favDailyRecyclerview.apply {
                adapter = FavDailyAdapter(daysList, context)
                layoutManager = LinearLayoutManager(context)
            }

        }
        }

 }


