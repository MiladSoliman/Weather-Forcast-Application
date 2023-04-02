package com.example.wetharapplication.favorite.model.myFav

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.location.Address
import android.location.Geocoder
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import androidx.recyclerview.widget.RecyclerView
import com.example.wetharapplication.R
import com.example.wetharapplication.databinding.FavItemBinding
import com.example.wetharapplication.model.MyResponse
import java.util.*

class FavAdapter (private val favWeather: List<MyResponse>, var context: Context , var listener : OnRemove , var click:OnClick) : RecyclerView.Adapter<FavAdapter.ViewHolder>() {

    lateinit var binding: FavItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding =FavItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      var response : MyResponse = favWeather.get(position)
        val geocoder = Geocoder(context, Locale.getDefault())
        var address:List<Address> = geocoder.getFromLocation(response.lat,response.lon,1) as List<Address>
        var area = address.get(0).adminArea
      var country = address.get(0).subAdminArea
       holder.binding.tvFavName.text = "$area $country"

       holder.binding.deleteImage.setOnClickListener {
           val yes = context.resources.getString(R.string.delete_accepet)
           val no = context.resources.getString(R.string.delete_refuse)
           val builder = AlertDialog.Builder(context)
           builder.setMessage(context.resources.getString(R.string.delete_message))
           builder.setTitle(context.resources.getString(R.string.delete_title))
           builder.setCancelable(false)
           builder.setPositiveButton(Html.fromHtml("<font color='#dad9d4'>$yes</font>"),
               { dialog: DialogInterface?, which: Int ->
                   listener.deleteCountry(response)
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

       var lat = response.lat
       var long = response.lon

       holder.binding.favCons.setOnClickListener {
           click.showDetails(lat , long)
       }
    }

    override fun getItemCount(): Int {
        return favWeather.size
    }

    inner class ViewHolder(var binding:FavItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }


   /* fun dialogSettingView() {
        var dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(com.example.wetharpresnter.R.layout.start_setting_dialog_iteam)
        val window: Window? = dialog.getWindow()
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawableResource(R.color.transparent);
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()


        dialog.findViewById<Button>(com.example.wetharpresnter.R.id.btn_save).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            var location =
                dialog.findViewById<RadioGroup>(com.example.wetharpresnter.R.id.rg_location)
            onRadioButtonClicked(location)
            var lang = dialog.findViewById<RadioGroup>(com.example.wetharpresnter.R.id.rg_lang)
            onRadioButtonClicked(lang)
            dialog.dismiss()

        }
    }*/
}