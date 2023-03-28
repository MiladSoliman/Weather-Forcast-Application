package com.example.wetharapplication.favorite.model.details

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wetharapplication.databinding.FavHourlyViewBinding
import com.example.wetharapplication.model.Current
import java.text.SimpleDateFormat
import java.util.*

class FavHourlyAdapter (private val hoursWeather: List<Current>, var context: Context) : RecyclerView.Adapter<FavHourlyAdapter.ViewHolder>() {

    lateinit var binding: FavHourlyViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding =  FavHourlyViewBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentHour = hoursWeather.get(position)
        var date= Date(currentHour.dt*1000L)
        var sdf= SimpleDateFormat("hh:mm a")
        sdf.timeZone= TimeZone.getDefault()
        var formatedData=sdf.format(date)
        holder.binding.tvFavoriteHour.text = formatedData.toString()
        holder.binding.tvFavoriteHourDegree.text = currentHour.temp.toString()
        Glide.with(context).load("https://openweathermap.org/img/wn/${currentHour.weather.get(0).icon}@2x.png").into(binding.favoriteHourImagView)
        Log.i("Mina" , currentHour.dt.toString() )
    }

    override fun getItemCount(): Int {
        return hoursWeather.size-24

    }

    inner class ViewHolder(var binding:FavHourlyViewBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}