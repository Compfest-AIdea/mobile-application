package com.compfest.aiapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.compfest.aiapplication.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            intent = Intent(this, HomeActivity::class.java)
            getUserName(intent)
            startActivity(intent)
        }
    }

    private fun getUserName(intent: Intent) {
        intent.putExtra("Name", binding.etName.text.toString())
        intent.putExtra("NickName", binding.etNickname.text.toString())
    }
}