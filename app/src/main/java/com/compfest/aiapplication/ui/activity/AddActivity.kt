package com.compfest.aiapplication.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.marginStart
import com.compfest.aiapplication.R
import com.compfest.aiapplication.databinding.ActivityAddBinding
import com.compfest.aiapplication.ui.fragment.AddFragmentOne
import com.compfest.aiapplication.ui.fragment.HomeFragment

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar1.ivLogo.visibility = View.GONE
        binding.toolbar1.tvAppName.text = "Add New"
        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, AddFragmentOne())
            .commit()
    }
}