package com.example.wetharapplication.home.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
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
import com.google.gson.Gson
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

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
     var myUtil :MyUtil = MyUtil()

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
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.title =
            requireActivity().getString(R.string.homeFragment)
        isMap = se?.getBoolean("Map", false)!!

        var context: Context = requireContext()
        if (isMap) {
            val action = HomeFragmentDirections.homtMap("home")
            Navigation.findNavController(requireView()).navigate(action)
        }
        lat = se?.getFloat("lat", 31.0f)?.toDouble()!!
        long = se?.getFloat("long", 31.0f)?.toDouble()!!
        language = se?.getString("language", "en")!!
        unites = se?.getString("units", "standard")!!
        homeFactory = HomeViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(),
                ConcreteLocalSource.getInstance(context)
            )
        )
        homeModel = ViewModelProvider(requireActivity(), homeFactory).get(HomeViewModel::class.java)

        if (InternetCheck.getConnectivity(context) == true) {


            homeModel.getWeather(lat, long, unites, language)
            lifecycleScope.launch {
                homeModel._myResponse.collectLatest { result ->
                    when (result) {
                        is ApiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            hideData()
                        }
                        is ApiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            response = result.data
                            se.edit().apply {
                                putString("HomeWeather",Gson().toJson(response))
                                apply()
                            }
                            showData()
                            setData()
                        }
                        else -> {
                            binding.progressBar.visibility = View.GONE
                            val snakbar = Snackbar.make(
                                requireView(),
                                context.resources.getString(R.string.no_internet),
                                Snackbar.LENGTH_LONG
                            ).setAction("Action", null)
                            snakbar.show()
                        }
                    }
                }
            }
        } else {
            binding.progressBar.visibility = View.GONE
            var backup = Gson().fromJson(se.getString("HomeWeather",""),MyResponse::class.java)
            if (se.contains("HomeWeather")){
                response = backup
                showData()
                setData()
                val snakbar = Snackbar.make(
                    requireView(),
                    context.resources.getString(R.string.snakbar_msg),
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snakbar.show()
            }else{
                hideData()
                val snakbar = Snackbar.make(
                    requireView(),
                    context.resources.getString(R.string.no_internet),
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snakbar.show()
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
        try{
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            var addressList:List<Address> = geocoder.getFromLocation(response.lat,response.lon,1) as List<Address>
            if (addressList.size != 0){
                var area = addressList.get(0).countryName
                var country = addressList.get(0).adminArea
                binding.tvCity.text = country +" , "+ area
            }else{
                binding.tvCity.text =response.timezone
            }
        }catch (e :IOException){
            binding.tvCity.text = response.timezone
        }catch (e: RemoteException){
            binding.tvCity.text = response.timezone
        }

        binding.tvDescription.text = response.current?.weather?.get(0)?.description
        var myCurrentDate = myUtil.convertDataAndTime(response.current?.dt)
        binding.tvDate.text = myCurrentDate
        var char = myUtil.getDegreeUnit(unites , language)
        var temp = String.format("%.0f", response.current?.temp)
        binding.tvHomedegree.text = temp + char
        binding.tvEditHumidity.text = response.current?.humidity.toString() + " " + "%"
        binding.tvEditCloud.text = response.current?.clouds.toString()
        binding.tvEditIsabilty.text = response.current?.visibility.toString()+ " " + "m"
        binding.tvEditPressur.text = response.current?.pressure.toString() + " " + "hpa"
        binding.tvEditUv.text = response.current?.uvi.toString()
        var wChar =myUtil.getWindSpeedUnit(unites,language)
        binding.tvEditWindspeed.text = response.current?.windSpeed.toString() + wChar
        var myimage = myUtil.cheangeIcon(response.current?.weather?.get(0)?.icon)
        binding.homeImage.setImageResource(myimage)
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


