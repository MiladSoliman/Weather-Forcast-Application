package com.example.wetharapplication.alert.model

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.wetharapplication.R
import com.example.wetharapplication.databinding.AlarmViewBinding


class AlertAdapter (private val alertsList: List<MyAlert>, var context: Context , var listener: DeleteAlert) : RecyclerView.Adapter<AlertAdapter.ViewHolder>() {
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
            val yes = context.resources.getString(R.string.delete_accepet)
            val no = context.resources.getString(R.string.delete_refuse)
            val builder = AlertDialog.Builder(context)
            builder.setMessage(context.resources.getString(R.string.delete_message))
            builder.setTitle(context.resources.getString(R.string.delete_title))
            builder.setCancelable(false)
            builder.setPositiveButton(
                Html.fromHtml("<font color='#dad9d4'>$yes</font>"),
                { dialog: DialogInterface?, which: Int ->
                    listener.deleteAlert(alert)
                    Toast.makeText(context, (context.resources.getString(R.string.delete_Toast)), Toast.LENGTH_SHORT).show()
                })
            builder.setNegativeButton(Html.fromHtml("<font color='#dad9d4'>$no</font>"),
                { dialog: DialogInterface, which: Int -> dialog.cancel() } )
            val alertDialog = builder.create()
            alertDialog.setOnShowListener {
                alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialog)
            }
            alertDialog.show()
        }
    }


    inner class ViewHolder(var binding:AlarmViewBinding) : RecyclerView.ViewHolder(binding.root) {

    }


    /*    val yes = context.resources.getString(R.string.delete_accepet)
           val no = context.resources.getString(R.string.delete_refuse)
           val builder = AlertDialog.Builder(context)
           builder.setMessage(context.resources.getString(R.string.delete_message))
           builder.setTitle(context.resources.getString(R.string.delete_title))
           builder.setCancelable(false)
           builder.setPositiveButton(Html.fromHtml("<font color='#dad9d4'>$yes</font>"),
               { dialog: DialogInterface?, which: Int ->
                    listener.deleteAlert(alert)
                   Toast.makeText(context, (context.resources.getString(R.string.delete_Toast)), Toast.LENGTH_SHORT).show()
               })
           builder.setNegativeButton(Html.fromHtml("<font color='#dad9d4'>$no</font>"),
               { dialog: DialogInterface, which: Int -> dialog.cancel() } )
           val alertDialog = builder.create()
           alertDialog.setOnShowListener {
               alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialog)
           }
           alertDialog.show()
       }*/


}