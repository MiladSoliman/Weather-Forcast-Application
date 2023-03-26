package com.example.wetharapplication.home.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.wetharapplication.R
import com.example.wetharapplication.databinding.FragmentHomeBinding
import com.example.wetharapplication.home.viewmodel.HomeViewModel
import com.example.wetharapplication.home.viewmodel.HomeViewModelFactory
import com.example.wetharapplication.model.Current
import com.example.wetharapplication.model.Daily
import com.example.wetharapplication.model.Location
import com.example.wetharapplication.model.Repository
import com.example.wetharapplication.network.WeatherClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class HomeFragment : Fragment() {
    lateinit var homeFactory: HomeViewModelFactory
    lateinit var homeModel: HomeViewModel
    lateinit var binding: FragmentHomeBinding
    lateinit var hoursList: List<Current>
    lateinit var daysList: List<Daily>
    var isMap :Boolean = false
    private var lat:Double =0.0
    private var long :Double =0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       var se =  activity?.getSharedPreferences("My Shared",MODE_PRIVATE)
        isMap = se?.getBoolean("Map",false) !!
        lat = se?.getFloat("lat" ,31.0f)?.toDouble() !!
        long =  se?.getFloat("long" ,31.0f)?.toDouble() !!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onResume() {
        var context: Context = requireContext()
        super.onResume()
        if (isMap) {
           Navigation.findNavController(requireView()).navigate(R.id.From_Home_To_Map)
           // val action = HomeFragmentDirections.actionHomeFragmentToMapsFragment("home")
          //  findNavController().navigate(action)
        } else {
            homeFactory =
                HomeViewModelFactory(Repository.getInstance(WeatherClient.getInstance()))
            homeModel =
                ViewModelProvider(requireActivity(), homeFactory).get(HomeViewModel::class.java)
            homeModel.getWeather(lat , long)
            homeModel._myResponse.observe(requireActivity()) { response ->
                if (response != null) {
                    Log.i("ya rab", "" + response.timezone)
                    binding.tvCity.text = response.timezone
                    binding.tvDescription.text = response.current.weather.get(0).description
                    var simpleDate = SimpleDateFormat("dd/M/yyyy - hh:mm:a ")
                    var currentDate = simpleDate.format(response.current.dt * 1000L)
                    binding.tvDate.text = currentDate.toString()
                    binding.tvHomedegree.text = response.current.temp.toString()
                    binding.tvEditHumidity.text = response.current.humidity.toString()
                    binding.tvEditCloud.text = response.current.clouds.toString()
                    binding.tvEditIsabilty.text = response.current.visibility.toString()
                    binding.tvEditPressur.text = response.current.pressure.toString() + " " + "hpa"
                    binding.tvEditUv.text = response.current.uvi.toString()
                    binding.tvEditWindspeed.text = response.current.windSpeed.toString()
                    Glide.with(requireContext())
                        .load("https://openweathermap.org/img/wn/${response.current.weather.get(0).icon}@2x.png")
                        .into(binding.homeImage)
                    Log.i("dayicon", "" + response.current.weather.get(0).icon)
                    hoursList = response.hourly
                    Log.i("Ehab", response.hourly.toString())
                    binding.hourlyRecyclerview.apply {
                        adapter = HourlyAdapter(hoursList, context)
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    }
                    daysList = response.daily
                    binding.dailyRecyclerview.apply {
                        adapter = DailyAdapter(daysList, context)
                        layoutManager = LinearLayoutManager(context)
                    }

                }
            }


        }
    }

}
