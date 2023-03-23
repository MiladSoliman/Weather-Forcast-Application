package com.example.wetharapplication.home.model

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wetharapplication.R
import com.example.wetharapplication.databinding.HourlyViewBinding
import com.example.wetharapplication.model.Current
import java.text.SimpleDateFormat
import java.util.*

class HourlyAdapter (private val hoursWeather: List<Current> , var context: Context) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

    lateinit var binding: HourlyViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = HourlyViewBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentHour = hoursWeather.get(position)
        var date= Date(currentHour.dt*1000L)
        var sdf= SimpleDateFormat("hh:mm a")
        sdf.timeZone=TimeZone.getDefault()
        var formatedData=sdf.format(date)
        holder.binding.tvHour.text = formatedData.toString()
        holder.binding.tvHourDegree.text = currentHour.temp.toString()
        Glide.with(context).load("https://openweathermap.org/img/wn/${currentHour.weather.get(0).icon}@2x.png").into(binding.hourImagView)
        Log.i("Mina" , currentHour.dt.toString() )
    }

    override fun getItemCount(): Int {
        return hoursWeather.size-24

    }

    inner class ViewHolder(var binding: HourlyViewBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}