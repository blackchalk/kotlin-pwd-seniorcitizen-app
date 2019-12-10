package com.seniorcitizen.app.ui.splash

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.seniorcitizen.app.R
import com.seniorcitizen.app.ui.MainActivity
import com.seniorcitizen.app.utils.Constants
import org.jetbrains.anko.startActivity

/**
 * Created by Nic Evans on 2019-12-10.
 */
class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({

            startActivity<MainActivity>()
            finish()

        }, Constants.SPLASH_TIME)

    }

}