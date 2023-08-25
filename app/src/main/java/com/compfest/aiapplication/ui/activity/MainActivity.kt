package com.compfest.aiapplication.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.compfest.aiapplication.R
import com.compfest.aiapplication.databinding.ActivityMainBinding
import com.compfest.aiapplication.manager.SessionManager
import com.compfest.aiapplication.ui.fragment.ArticlesFragment
import com.compfest.aiapplication.ui.fragment.HomeFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        val userName = sessionManager.getNickname()
        if (!userName.isNullOrBlank()) {
            binding.toolbar1.tvAppName.text = "Hello,".plus(userName.lowercase())
        }

        binding.bottomNavView.menu.getItem(1).isEnabled = false
        binding.bottomNavView.setOnItemSelectedListener(this)
        binding.fabAddNew.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, HomeFragment())
            .commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                supportFragmentManager.beginTransaction()
                    .replace(binding.fragmentContainer.id, HomeFragment())
                    .commit()
                true
            }
            R.id.articles -> {
                supportFragmentManager.beginTransaction()
                    .replace(binding.fragmentContainer.id, ArticlesFragment())
                    .commit()
                true
            }
            else -> false
        }
    }
}