package com.example.wetharapplication.alert

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wetharapplication.databinding.AlarmViewBinding


class AlertAdapter (private val alertsList: List<MyAlert>, var listener:DeleteAlert) : RecyclerView.Adapter<AlertAdapter.ViewHolder>() {
    lateinit var binding: AlarmViewBinding



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = AlarmViewBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
      return  alertsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var alert = alertsList.get(position)
        holder.binding.fDate.text = alert.startDay
        holder.binding.fTime.text = alert.startTime
        holder.binding.tDate.text = alert.endDay
        holder.binding.tTime.text = alert.endTime
        holder.binding.deleteAlert.setOnClickListener {
            listener.deleteAlert(alert)
        }
    }


    inner class ViewHolder(var binding:AlarmViewBinding) : RecyclerView.ViewHolder(binding.root) {

    }


}