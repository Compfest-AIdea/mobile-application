package com.compfest.aiapplication.ui.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.compfest.aiapplication.data.PredictionImageInput
import com.compfest.aiapplication.data.PredictionTabularInput
import com.compfest.aiapplication.data.PredictionTabularResult
import com.compfest.aiapplication.databinding.ActivityResultBinding
import com.compfest.aiapplication.model.ResultViewModel
import com.compfest.aiapplication.model.ViewModelFactory

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var viewModel: ResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar1.ivLogo.visibility = View.GONE
        binding.toolbar1.tvAppName.text = "Result"

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ResultViewModel::class.java]

        val predictionInput = getParcelize(EXTRA_PREDICTION_TABULAR_INPUT) as PredictionTabularInput?
        val tabularResult = getParcelize(EXTRA_RESULT_TABULAR) as PredictionTabularResult?
        if (predictionInput != null && tabularResult != null) {
            Log.d("MainActivity", predictionInput.toString())
            viewModel.savePredictionInput(predictionInput)
            viewModel.savePredictionTabularResult(tabularResult)
        }
    }

    companion object {
        const val EXTRA_RESULT_IMAGE = "extra_result_image"
        const val EXTRA_RESULT_TABULAR = "extra_result_tabular"
        const val EXTRA_PREDICTION_TABULAR_INPUT = "extra_prediction_tabular_input"
        const val EXTRA_PREDICTION_IMAGE_INPUT = "extra_prediction_image_input"
    }

    @Suppress("DEPRECATION")
    private fun getParcelize(extra: String): Any? {
        when (extra) {
            EXTRA_PREDICTION_TABULAR_INPUT -> {
                val predictionTabularInput = if (Build.VERSION.SDK_INT >= 33) {
                    intent.getParcelableExtra(EXTRA_PREDICTION_TABULAR_INPUT, PredictionTabularInput::class.java)
                } else {
                    intent.getParcelableExtra(EXTRA_PREDICTION_TABULAR_INPUT)
                }
                return predictionTabularInput
            }
            EXTRA_PREDICTION_IMAGE_INPUT -> {
                val predictionImageInput = if (Build.VERSION.SDK_INT >= 33) {
                    intent.getParcelableExtra(EXTRA_PREDICTION_IMAGE_INPUT, PredictionImageInput::class.java)
                } else {
                    intent.getParcelableExtra(EXTRA_PREDICTION_IMAGE_INPUT)
                }
                return predictionImageInput
            }
            EXTRA_RESULT_TABULAR -> {
                val predictionTabularResult = if (Build.VERSION.SDK_INT >= 33) {
                    intent.getParcelableExtra(EXTRA_RESULT_TABULAR, PredictionTabularResult::class.java)
                } else {
                    intent.getParcelableExtra(EXTRA_RESULT_TABULAR)
                }
                return predictionTabularResult
            }
            else -> { return null }
        }
    }
}