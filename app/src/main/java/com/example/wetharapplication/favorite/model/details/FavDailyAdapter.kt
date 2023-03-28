package com.example.wetharapplication.favorite.model.details

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wetharapplication.databinding.FavDailyViewBinding
import com.example.wetharapplication.model.Daily
import java.text.SimpleDateFormat
import java.util.*

class FavDailyAdapter (private val dailyWeather: List<Daily>, var context: Context) : RecyclerView.Adapter<FavDailyAdapter.ViewHolder>() {

    lateinit var binding: FavDailyViewBinding



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding =FavDailyViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentDay = dailyWeather.get(position)
        var date= Date(currentDay.dt*1000L)
        var sdf= SimpleDateFormat("d")
        sdf.timeZone= TimeZone.getDefault()
        var formatedData=sdf.format(date)
        var intDay=formatedData.toInt()
        var calendar= Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH,intDay)
        var format= SimpleDateFormat("EEEE")
        var day=format.format(calendar.time)
        holder.binding.tvFavoriteDay.text = day
        Glide.with(context).load("https://openweathermap.org/img/wn/${currentDay.weather.get(0).icon}@2x.png").into(binding.favoriteDayilyImageView)
        holder.binding.tvFavoriteDayDesc.text =currentDay.weather.get(0).description
        // holder.binding.tvDailyMaxDegree.text = "/"+currentDay.temp.max.toString()
        holder.binding.tvDayFavoriteMinDegree.text = currentDay.temp.min.toString() +  "/" +currentDay.temp.max.toString() + "K"

        //tv_daily_max_degree
    }

    override fun getItemCount(): Int {
        return dailyWeather.size

    }

    inner class ViewHolder(var binding:FavDailyViewBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}