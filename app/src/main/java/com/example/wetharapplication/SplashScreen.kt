package com.example.wetharapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wetharapplication.dialog.model.IntialDialogFragment

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        var dialog = IntialDialogFragment()
        dialog.show(supportFragmentManager , "Milad")
    }
}