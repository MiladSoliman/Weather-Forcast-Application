package com.example.wetharapplication.home.model

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wetharapplication.R
import com.example.wetharapplication.databinding.DailyViewBinding
import com.example.wetharapplication.databinding.HourlyViewBinding
import com.example.wetharapplication.model.Current
import com.example.wetharapplication.model.Daily
import com.example.wetharapplication.util.MyUtil
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter (private val dailyWeather: List<Daily> , var context: Context) : RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    lateinit var binding: DailyViewBinding



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DailyViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentDay = dailyWeather.get(position)
        var day = MyUtil().ConvertToDay(currentDay.dt)
        holder.binding.tvDay.text = day
        Glide.with(context).load("https://openweathermap.org/img/wn/${currentDay.weather.get(0).icon}@2x.png").into(binding.dayilyImageView)
        holder.binding.tvDayDesc.text =currentDay.weather.get(0).description
       // holder.binding.tvDailyMaxDegree.text = "/"+currentDay.temp.max.toString()
        var minTemp = String.format("%.0f",currentDay.temp.min)
        var maxTemp = String.format("%.0f",currentDay.temp.max)

        //holder.binding.tvDayMinDegree.text = currentDay.temp.min.toString() +  "/" +currentDay.temp.max.toString()
        holder.binding.tvDayMinDegree.text =minTemp + "" + "/" +""+ maxTemp
        //tv_daily_max_degree
    }

    override fun getItemCount(): Int {
        return dailyWeather.size

    }

    inner class ViewHolder(var binding: DailyViewBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
