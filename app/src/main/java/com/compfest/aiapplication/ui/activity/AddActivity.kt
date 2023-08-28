package com.compfest.aiapplication.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.compfest.aiapplication.databinding.ActivityAddBinding
import com.compfest.aiapplication.ui.fragment.AddFragmentOne

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

    companion object {
        const val CAMERA_X_RESULT = 200
    }
}