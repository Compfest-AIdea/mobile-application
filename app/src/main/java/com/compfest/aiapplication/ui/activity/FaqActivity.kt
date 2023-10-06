package com.compfest.aiapplication.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.compfest.aiapplication.databinding.ActivityFaqBinding

class FaqActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFaqBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}