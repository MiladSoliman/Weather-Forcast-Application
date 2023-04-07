package com.example.wetharapplication.home.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.wetharapplication.R
import com.example.wetharapplication.database.ConcreteLocalSource
import com.example.wetharapplication.databinding.FragmentHomeBinding
import com.example.wetharapplication.home.viewmodel.HomeViewModel
import com.example.wetharapplication.home.viewmodel.HomeViewModelFactory
import com.example.wetharapplication.model.*
import com.example.wetharapplication.network.InternetCheck
import com.example.wetharapplication.network.WeatherClient
import com.example.wetharapplication.util.MyUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class HomeFragment : Fragment() {
    lateinit var homeFactory: HomeViewModelFactory
    lateinit var homeModel: HomeViewModel
    lateinit var binding: FragmentHomeBinding
    lateinit var hoursList: List<Current>
    lateinit var daysList: List<Daily>
    var isMap: Boolean = false
    private var lat: Double = 0.0
    private var long: Double = 0.0
    lateinit var se: SharedPreferences
    lateinit var language: String
    lateinit var unites: String
    lateinit var response :MyResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        se = activity?.getSharedPreferences("My Shared", MODE_PRIVATE)!!


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
        isMap = se?.getBoolean("Map", false)!!
        var context: Context = requireContext()
        super.onResume()
        if (isMap) {
            val action = HomeFragmentDirections.homtMap("home")
            Navigation.findNavController(requireView()).navigate(action)
        }
            lat = se?.getFloat("lat", 31.0f)?.toDouble()!!
            long = se?.getFloat("long", 31.0f)?.toDouble()!!
            language = se?.getString("language", "en")!!
            unites = se?.getString("units", "standard")!!
            homeFactory = HomeViewModelFactory(Repository.getInstance(WeatherClient.getInstance(), ConcreteLocalSource.getInstance(context)))
            homeModel = ViewModelProvider(requireActivity(), homeFactory).get(HomeViewModel::class.java)

        //check internet
                homeModel.getWeather(lat, long, unites, language)


            lifecycleScope.launch {
                homeModel._myResponse.collectLatest { result->
                    when(result){
                        is ApiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            hideData()
                        }
                        is ApiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            response = result.data
                            showData()
                            setData()
                        }
                        else ->{
                            binding.progressBar.visibility = View.GONE
                            val snakbar = Snackbar.make(requireView(), "There is an Erorr , Please check your Network", Snackbar.LENGTH_LONG).setAction("Action", null)
                            snakbar.show()
                        }
                    }
                }
            }


        }


    private fun hideData(){
        binding.tvCity.visibility = View.GONE
        binding.tvDate.visibility = View.GONE
        binding.tvDescription.visibility = View.GONE
        binding.degreedCard.visibility = View.GONE
        binding.cardView7.visibility = View.GONE
        binding.cardView9.visibility = View.GONE
        binding.cardView10.visibility = View.GONE
        binding.cardView11.visibility = View.GONE
        binding.cardView12.visibility = View.GONE
        binding.cardView13.visibility = View.GONE
        binding.hourlyRecyclerview.visibility = View.GONE
        binding.dailyRecyclerview.visibility=View.GONE


    }

    private fun showData() {
        binding.tvCity.visibility = View.VISIBLE
        binding.tvDate.visibility = View.VISIBLE
        binding.tvDescription.visibility = View.VISIBLE
        binding.degreedCard.visibility = View.VISIBLE
        binding.cardView7.visibility = View.VISIBLE
        binding.cardView9.visibility = View.VISIBLE
        binding.cardView10.visibility = View.VISIBLE
        binding.cardView11.visibility = View.VISIBLE
        binding.cardView12.visibility = View.VISIBLE
        binding.cardView13.visibility = View.VISIBLE
        binding.hourlyRecyclerview.visibility = View.VISIBLE
        binding.dailyRecyclerview.visibility=View.VISIBLE
    }

    private fun setData(){
        binding.tvCity.text = response.timezone
        binding.tvDescription.text = response.current?.weather?.get(0)?.description
        var myCurrentDate = MyUtil().convertDataAndTime(response.current?.dt)
        binding.tvDate.text = myCurrentDate
        var char = MyUtil().getDegreeUnit(unites , language)
        var temp = String.format("%.0f", response.current?.temp)
        binding.tvHomedegree.text = temp + char
        binding.tvEditHumidity.text = response.current?.humidity.toString() + " " + "%"
        binding.tvEditCloud.text = response.current?.clouds.toString()
        binding.tvEditIsabilty.text = response.current?.visibility.toString()+ " " + "m"
        binding.tvEditPressur.text = response.current?.pressure.toString() + " " + "hpa"
        binding.tvEditUv.text = response.current?.uvi.toString()
        var wChar = MyUtil().getWindSpeedUnit(unites,language)
        binding.tvEditWindspeed.text = response.current?.windSpeed.toString() + wChar
        Glide.with(requireContext())
            .load("https://openweathermap.org/img/wn/${response.current?.weather?.get(0)?.icon}@2x.png")
            .into(binding.homeImage)
        hoursList = response.hourly

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


