package com.example.wetharapplication.favorite.model.details

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wetharapplication.databinding.FavHourlyViewBinding
import com.example.wetharapplication.model.Current
import com.example.wetharapplication.util.MyUtil
import java.text.SimpleDateFormat
import java.util.*

class FavHourlyAdapter (private val hoursWeather: List<Current>, var context: Context) : RecyclerView.Adapter<FavHourlyAdapter.ViewHolder>() {

    lateinit var binding: FavHourlyViewBinding
    var myUtil = MyUtil()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding =  FavHourlyViewBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentHour = hoursWeather.get(position)
        var formatedData = myUtil.convertToHour(currentHour.dt)
        holder.binding.tvFavoriteHour.text = formatedData
        var temp = String.format("%.0f",currentHour.temp)
        holder.binding.tvFavoriteHourDegree.text = temp
        binding.favoriteHourImagView.setImageResource(myUtil.cheangeIcon(currentHour.weather.get(0).icon))
    }

    override fun getItemCount(): Int {
        return hoursWeather.size-24
    }

    inner class ViewHolder(var binding:FavHourlyViewBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}