package com.gadgetsfury.electionindia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.gadgetsfury.electionindia.home.HomeActivity
import com.gadgetsfury.electionindia.slides.SlideActivity
import com.gadgetsfury.electionindia.util.SharedPref

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({

            if (SharedPref(this).isFirstTime()) {
                startActivity(Intent(this, SlideActivity::class.java))
            } else {
                startActivity(Intent(this, HomeActivity::class.java))
            }
            finish()

        }, 1500)

    }
}
