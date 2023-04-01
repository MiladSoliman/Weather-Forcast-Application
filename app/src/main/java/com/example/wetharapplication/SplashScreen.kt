package com.example.wetharapplication

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wetharapplication.dialog.model.IntialDialogFragment
import java.util.*

class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
      /*  var languageSettngs =  this.getSharedPreferences("language",MODE_PRIVATE)
        var language = languageSettngs?.getString("My Lang","en")
         setLocale(language)*/


        var dialog = IntialDialogFragment()
        dialog.show(supportFragmentManager , "Milad")
    }


  /*  private fun setLocale(lang: String?) {
        var locale = Locale(lang)
        Locale.setDefault(locale)
        var config = Configuration()
        config.setLocale(locale)
        this.resources?.updateConfiguration(config,this.resources?.displayMetrics)
        var languageSettngs = this.getSharedPreferences("language", MODE_PRIVATE)?.edit()?.apply{
            putString("My Lang",lang)
            apply()
        }*/
}




