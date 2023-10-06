package com.compfest.aiapplication.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.compfest.aiapplication.STORAGE_ACCESS_PERMISSION_REQUEST
import com.compfest.aiapplication.databinding.ActivityAddBinding
import com.compfest.aiapplication.model.AddViewModel
import com.compfest.aiapplication.ui.fragment.AddFragmentOne

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private val viewModel: AddViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar1.ivLogo.visibility = View.GONE
        binding.toolbar1.tvAppName.text = "Tambah Data"
        binding.toolbar1.btnGoHelp.setOnClickListener {
            startActivity(Intent(this, FaqActivity::class.java))
        }

        val fragment = AddFragmentOne()
        fragment.arguments = Bundle().apply { putString("Fragment", fragment::class.java.simpleName) }
        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, fragment)
            .commit()

        showLogging(viewModel.viewModelName)

        if (!checkPermission()) {
            requestPermission()
        }
    }

    private fun checkPermission(): Boolean {
        val readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return (readPermission == PackageManager.PERMISSION_GRANTED) && (writePermission == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            STORAGE_ACCESS_PERMISSION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_ACCESS_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLogging(msg: String) {
        Log.d(AddActivity::class.java.simpleName, msg)
    }
}