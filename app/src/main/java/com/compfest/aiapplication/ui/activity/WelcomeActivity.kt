package com.compfest.aiapplication.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
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
            val textName = binding.etName.text
            val textNickname = binding.etNickname.text
            checkField(textName, textNickname)
        }
    }

    private fun checkField(textName: Editable?, textNickname: Editable?) {
        if (textName.isNullOrEmpty() && textNickname.isNullOrEmpty()) {
            showToast("Please, fill in the input field")
        } else if (textName.isNullOrEmpty()) {
            showToast("Please, fill in your name")
        } else if (textNickname.isNullOrEmpty()) {
            showToast("Please, fill in your nickname")
        } else {
            sessionManager.saveSession(textName.toString(), textNickname.toString())
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}