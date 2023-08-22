package com.compfest.aiapplication.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.compfest.aiapplication.databinding.ActivityWelcomeBinding
import com.compfest.aiapplication.manager.SessionManager

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.btnNext.setOnClickListener {
            sessionManager.saveSession(binding.etName.text.toString(), binding.etNickname.text.toString())
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}