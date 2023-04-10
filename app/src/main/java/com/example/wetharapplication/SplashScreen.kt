package com.example.wetharapplication

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.wetharapplication.databinding.ActivitySplashScreenBinding
import com.example.wetharapplication.dialog.model.IntialDialogFragment
import java.util.*

class SplashScreen : AppCompatActivity() {
     lateinit var binding: ActivitySplashScreenBinding

    lateinit var set: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
       var set = getSharedPreferences("My Shared", MODE_PRIVATE)
       var isFirst = set.getBoolean("isFirstTime",false)


        var dialog = IntialDialogFragment()

     binding.welcomeSplash.animate().setDuration(10000).setStartDelay(1500);

        Handler().postDelayed({
            binding.welcomeSplash.visibility = View.GONE
            if (isFirst==false) {
                dialog.show(supportFragmentManager, "Milad")
            } else{
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 4500)


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




