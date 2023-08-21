package com.compfest.aiapplication.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.compfest.aiapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavView.menu.getItem(1).isEnabled = false
    }
}