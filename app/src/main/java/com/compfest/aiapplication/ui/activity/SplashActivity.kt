package com.compfest.aiapplication.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.compfest.aiapplication.LOGIN_PREF
import com.compfest.aiapplication.LOGIN_STATUS
import com.compfest.aiapplication.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val hasLoggedInBefore = getSharedPreferences(LOGIN_PREF, Context.MODE_PRIVATE).getBoolean(LOGIN_STATUS, false)
        Handler(Looper.getMainLooper()).postDelayed({
            if (hasLoggedInBefore) {
                toMain()
            } else {
                toWelcome()
            }
        }, SPLASH_SCREEN_DELAY)
    }

    private fun toWelcome() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }

    private fun toMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    companion object {
        private const val SPLASH_SCREEN_DELAY = 3000L
    }
}