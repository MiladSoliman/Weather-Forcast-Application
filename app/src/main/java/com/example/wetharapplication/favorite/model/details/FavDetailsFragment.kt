package com.example.wetharapplication.favorite.model.details

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.wetharapplication.network.WeatherClient
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
      // check for Network
        detilsModel.getWeatherFromApi(latitude,longitude)

        detilsModel._FavWeathers.observe(requireActivity()){response->
            binding.tvFavCity.text = response.timezone
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


