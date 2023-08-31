package com.compfest.aiapplication.ui.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.compfest.aiapplication.R
import com.compfest.aiapplication.data.Prediction
import com.compfest.aiapplication.model.ResultViewModel
import com.compfest.aiapplication.model.ViewModelFactory

class ResultActivity : AppCompatActivity() {
    private lateinit var viewModel: ResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ResultViewModel::class.java]

        val predictionInput = getParcelize()
        if (predictionInput != null) {
            Log.d("MainActivity", predictionInput.toString())
        }
    }

    companion object {
        const val EXTRA_RESULT_IMAGE = "extra_result_image"
        const val EXTRA_RESULT_TABULA = "extra_result_tabular"
        const val EXTRA_PREDICTION_INPUT = "extra_prediction_input"
    }

    @Suppress("DEPRECATION")
    private fun getParcelize(): Prediction? {
        val predictionInput = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_PREDICTION_INPUT, Prediction::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_PREDICTION_INPUT)
        }
        return predictionInput
    }
}