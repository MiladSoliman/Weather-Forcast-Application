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
import androidx.navigation.Navigation
import com.example.wetharapplication.R
import com.example.wetharapplication.databinding.FragmentSettingsBinding
import org.intellij.lang.annotations.Language
import java.util.*


class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    lateinit var set: SharedPreferences
    var isMap : Boolean =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        setDefultSettings()
        //langauge
        binding.radioArabic.setOnClickListener {
            set.edit().putString("language","ar").apply()
            setLocale("ar")
            activity?.recreate()
        }
        binding.radioEnglish.setOnClickListener {
            set.edit().putString("language","en").apply()
            setLocale("en")
            activity?.recreate()
        }


        //Unites
        binding.radioStandard.setOnClickListener {
            set.edit().putString("units","standard").apply()
        }
        binding.radioImprial.setOnClickListener {
            set.edit().putString("units","imperial").apply()
        }

        binding.radioMetric.setOnClickListener {
            set.edit().putString("units","metric").apply()
        }
        //map
        binding.radioMap.setOnClickListener {
            set.edit().putBoolean("Map",true).apply()
            Navigation.findNavController(requireView()).navigate(R.id.settingstomap)
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

  /* private fun getLocal(){
        var languageSettngs =  activity?.getSharedPreferences("language",MODE_PRIVATE)
        var language = languageSettngs?.getString("language","en")
        setLocale(language)
    }*/


   fun setDefultSettings(){
      binding.radioStandard.isChecked = true
       binding.radioEnglish.isChecked = true
       if (isMap==true){
           binding.radioMap.isChecked =true
       }else{
           binding.radioGps.isChecked = true
       }
   }

}