package com.example.wetharapplication

import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wetharapplication.dialog.model.IntialDialogFragment
import java.util.*

class SplashScreen : AppCompatActivity() {

    lateinit var set: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        set =  this.getSharedPreferences("My Shared",MODE_PRIVATE)
        var language = set.getString("My Lang","en")
         setLocale(language)


        var dialog = IntialDialogFragment()
        dialog.show(supportFragmentManager , "Milad")
    }


  private fun setLocale(lang: String?) {
       var locale = Locale(lang)
        Locale.setDefault(locale)
        var config = Configuration()
        config.setLocale(locale)
         this.resources?.updateConfiguration(config,this.resources?.displayMetrics)
        set.edit().putString("My Lang" , lang).apply()
    }

}




