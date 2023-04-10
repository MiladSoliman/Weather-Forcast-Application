package com.example.wetharapplication.settings

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.wetharapplication.R
import com.example.wetharapplication.databinding.FragmentSettingsBinding
import com.example.wetharapplication.model.Location
import com.example.wetharapplication.network.InternetCheck
import com.google.android.material.snackbar.Snackbar
import org.intellij.lang.annotations.Language
import java.util.*


class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    lateinit var set: SharedPreferences
    var isMap : Boolean =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.title =
            requireActivity().getString(R.string.settingsFragement)
       //  getLocal()

        set = activity?.getSharedPreferences("My Shared",MODE_PRIVATE)!!
        isMap = set?.getBoolean("Map",false) !!

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var context = requireContext()
        setDefultSettings()
        //langauge
      if(InternetCheck.getConnectivity(context)==true){
          binding.radioArabic.setOnClickListener {
              set.edit().putString("language", "ar").apply()
              setLocale("ar")
              activity?.recreate()
          }
          binding.radioEnglish.setOnClickListener {
              set.edit().putString("language", "en").apply()
              setLocale("en")
              activity?.recreate()
          }
      }else{
          val snakbar = Snackbar.make(
              view,
              context.resources.getString(R.string.no_internet),
              Snackbar.LENGTH_LONG
          ).setAction("Action", null)
          snakbar.show()
      }



        //Unites
        if (InternetCheck.getConnectivity(context) == true){
            binding.radioStandard.setOnClickListener {
                set.edit().putString("units", "standard").apply()
            }
        binding.radioImprial.setOnClickListener {
            set.edit().putString("units", "imperial").apply()
        }

        binding.radioMetric.setOnClickListener {
            set.edit().putString("units", "metric").apply()
        }
    }else{
            val snakbar = Snackbar.make(
                view,
                context.resources.getString(R.string.no_internet),
                Snackbar.LENGTH_LONG
            ).setAction("Action", null)
            snakbar.show()
        }
        //map
        binding.radioMap.setOnClickListener {
            if(InternetCheck.getConnectivity(context)==true){
                set.edit().putBoolean("Map",true).apply()
                Navigation.findNavController(requireView()).navigate(R.id.settingstomap)
            }else{
                val snakbar = Snackbar.make(
                    view,
                    context.resources.getString(R.string.no_internet),
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snakbar.show()
            }

        }
        //Location
        binding.radioGps.setOnClickListener {
            var loc = Location(requireContext())
            Log.i("GPS","ana hna")
            loc.getLastLocation()
            loc._myLocation.observe(viewLifecycleOwner) {
                set.edit().apply {
                    putFloat("lat",it[0].toFloat())
                    putFloat("long",it[1].toFloat())
                    apply()
                }
                Log.i("GPSlat",""+it[0].toFloat())
                Log.i("GPSlong",""+it[1].toFloat())
            }
        }

        binding.radioEnable.setOnClickListener {
            set.edit().apply {
                putBoolean("isEnabled",true)
                apply()
            }
        }

        binding.radioDisable.setOnClickListener {
            set.edit().apply(){
                putBoolean("isEnabled",false)
                apply()
            }
        }

    }


    private fun setLocale(lang: String?) {
       var locale = Locale(lang)
        Locale.setDefault(locale)
        var config = Configuration()
        config.setLocale(locale)
        context?.resources?.updateConfiguration(config,context?.resources?.displayMetrics)
        set.edit().putString("My Lang" , lang).apply()
    }


   fun setDefultSettings(){
      binding.radioStandard.isChecked = true
       binding.radioEnglish.isChecked = true
       binding.radioEnable.isChecked = true

   }

}