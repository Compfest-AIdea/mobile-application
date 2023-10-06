package com.compfest.aiapplication.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.compfest.aiapplication.R
import com.compfest.aiapplication.data.PredictionImageInput
import com.compfest.aiapplication.data.PredictionImageResult
import com.compfest.aiapplication.data.PredictionResult
import com.compfest.aiapplication.data.PredictionTabularInput
import com.compfest.aiapplication.data.PredictionTabularResult
import com.compfest.aiapplication.databinding.ActivityResultBinding
import com.compfest.aiapplication.databinding.BottomSheetDetailBinding
import com.compfest.aiapplication.getCurrentTimeMillis
import com.compfest.aiapplication.getImageFromExternalStorage
import com.compfest.aiapplication.model.ResultViewModel
import com.compfest.aiapplication.model.ViewModelFactory
import com.compfest.aiapplication.ui.fragment.HomeFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var viewModel: ResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar1.ivLogo.visibility = View.GONE
        binding.toolbar1.tvAppName.text = "Hasil Pemeriksaan"
        binding.toolbar1.btnGoHelp.setOnClickListener {
            startActivity(Intent(this, FaqActivity::class.java).putExtra(FaqActivity.ORIGIN, ResultActivity::class.java.simpleName))
        }

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ResultViewModel::class.java]

        val origin = intent.getStringExtra("origin") as String
        val id = intent.getIntExtra(EXTRA_ID, 0)
        if (origin == HomeFragment::class.java.simpleName) {
            val predictionResult = viewModel.getPredictionResult(id)
            predictionResult.observe(this) {
                setOutputResult(it.resultHairLoss, it.resultScalpCondi, it.imgPath)
                setBottomSheetDetail(it.resultScalpCondi)
            }
        } else {
            val predictionTabularInput = getParcelize(EXTRA_PREDICTION_TABULAR_INPUT) as PredictionTabularInput?
            val predictionImageInput = getParcelize(EXTRA_PREDICTION_IMAGE_INPUT) as PredictionImageInput?
            val tabularResult = getParcelize(EXTRA_RESULT_TABULAR) as PredictionTabularResult?
            val imageResult = getParcelize(EXTRA_RESULT_IMAGE) as PredictionImageResult?
            if (predictionTabularInput != null && tabularResult != null && imageResult != null && predictionImageInput != null) {
                Log.d("MainActivity", predictionTabularInput.toString())
                val resultTabular = setOutputTabularResult(tabularResult)
                val resultImage = setOutputImageResult(imageResult)
                val imagePath = predictionImageInput.imagePath
                viewModel.saveInputData(predictionTabularInput, predictionImageInput)
                setOutputResult(resultTabular, resultImage, imagePath)
                setBottomSheetDetail(resultImage)
                val predictionResult = PredictionResult(resultHairLoss = resultTabular, resultScalpCondi = resultImage, imgPath = imagePath, timeTaken = getCurrentTimeMillis())
                viewModel.saveResult(predictionResult)
            }
        }
    }

    companion object {
        const val EXTRA_RESULT_IMAGE = "extra_result_image"
        const val EXTRA_RESULT_TABULAR = "extra_result_tabular"
        const val EXTRA_PREDICTION_TABULAR_INPUT = "extra_prediction_tabular_input"
        const val EXTRA_PREDICTION_IMAGE_INPUT = "extra_prediction_image_input"
        const val EXTRA_ID = "extra_id"
    }

    private fun setOutputResult(resultTabularPrediction: String, resultImagePrediction: String, imagePath: String) {
        binding.ivImageResult.setImageBitmap(getImageFromExternalStorage(imagePath))
        binding.tvResultTabular.text = resultTabularPrediction
        binding.tvResultImage.text = resultImagePrediction
    }

    @Suppress("DEPRECATION")
    private fun getParcelize(extra: String): Any? {
        when (extra) {
            EXTRA_PREDICTION_TABULAR_INPUT -> {
                val parcelable = if (Build.VERSION.SDK_INT >= 33) {
                    intent.getParcelableExtra(EXTRA_PREDICTION_TABULAR_INPUT, PredictionTabularInput::class.java)
                } else {
                    intent.getParcelableExtra(EXTRA_PREDICTION_TABULAR_INPUT)
                }
                return parcelable
            }
            EXTRA_PREDICTION_IMAGE_INPUT -> {
                val parcelable = if (Build.VERSION.SDK_INT >= 33) {
                    intent.getParcelableExtra(EXTRA_PREDICTION_IMAGE_INPUT, PredictionImageInput::class.java)
                } else {
                    intent.getParcelableExtra(EXTRA_PREDICTION_IMAGE_INPUT)
                }
                return parcelable
            }
            EXTRA_RESULT_TABULAR -> {
                val parcelable = if (Build.VERSION.SDK_INT >= 33) {
                    intent.getParcelableExtra(EXTRA_RESULT_TABULAR, PredictionTabularResult::class.java)
                } else {
                    intent.getParcelableExtra(EXTRA_RESULT_TABULAR)
                }
                return parcelable
            }
            EXTRA_RESULT_IMAGE -> {
                val parcelable = if (Build.VERSION.SDK_INT >= 33) {
                    intent.getParcelableExtra(EXTRA_RESULT_IMAGE, PredictionImageResult::class.java)
                } else {
                    intent.getParcelableExtra(EXTRA_RESULT_IMAGE)
                }
                return parcelable
            }
            else -> { return null }
        }
    }

    private fun setOutputTabularResult(predictionTabularResult: PredictionTabularResult): String {
        val tabularResult = arrayListOf(
            predictionTabularResult.class1,
            predictionTabularResult.class2,
            predictionTabularResult.class3,
            predictionTabularResult.class4
        )
        var maxVal = tabularResult[0]
        for (i in 1 until tabularResult.size) {
            val currentVal = tabularResult[i]

            if (currentVal > maxVal) {
                maxVal = currentVal
            }
        }
        val maxValIndex = tabularResult.indexOf(maxVal)
        when (maxValIndex) {
            3 -> { return "Serious Hair Loss" }
            2 -> { return "Hair Loss" }
            1 -> { return "Slight Hair Loss" }
            else -> { return "Normal" }
        }
    }

    private fun setOutputImageResult(predictionImageResult: PredictionImageResult): String {
        val imageResult = arrayListOf(
            predictionImageResult.class1,
            predictionImageResult.class2,
            predictionImageResult.class3,
            predictionImageResult.class4,
            predictionImageResult.class5
        )
        var maxValIndex = 0
        var maxVal = imageResult[maxValIndex]
        for (i in 1 until imageResult.size) {
            if (imageResult[i] > maxVal) {
                maxVal = imageResult[i]
                maxValIndex = i
            }
        }
        val disesaseName = resources.getStringArray(R.array.disease_name)
        when (maxValIndex) {
            4 -> { return disesaseName[0] }
            3 -> { return disesaseName[1] }
            2 -> { return disesaseName[2] }
            1 -> { return "normal" }
            else -> { return disesaseName[3] }
        }
    }

    private fun setBottomSheetDetail(imageResult: String) {
        val bottomSheetDetail: View = findViewById(R.id.bottom_sheet)
        val bottomSheetBinding = BottomSheetDetailBinding.bind(bottomSheetDetail)
        val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheetDetail)
        val diseaseName = resources.getStringArray(R.array.disease_name)
        val diseaseDesc = resources.getStringArray(R.array.disease_desc)
        val diseaseArticle = resources.getStringArray(R.array.disease_article)
        when (imageResult) {
            diseaseName[0] -> {
                bottomSheetBinding.tvDiseaseArticleTitle.text = diseaseName[0]
                bottomSheetBinding.tvDisesaseArticleDesc.text = diseaseDesc[0]
                goToDetailLink(diseaseArticle[0], bottomSheetBinding)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            diseaseName[1] -> {
                bottomSheetBinding.tvDiseaseArticleTitle.text = diseaseName[1]
                bottomSheetBinding.tvDisesaseArticleDesc.text = diseaseDesc[1]
                goToDetailLink(diseaseArticle[1], bottomSheetBinding)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            diseaseName[2] -> {
                bottomSheetBinding.tvDiseaseArticleTitle.text = diseaseName[2]
                bottomSheetBinding.tvDisesaseArticleDesc.text = diseaseDesc[2]
                goToDetailLink(diseaseArticle[2], bottomSheetBinding)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            diseaseName[3] -> {
                bottomSheetBinding.tvDiseaseArticleTitle.text = diseaseName[3]
                bottomSheetBinding.tvDisesaseArticleDesc.text = diseaseDesc[3]
                goToDetailLink(diseaseArticle[3], bottomSheetBinding)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            else -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                bottomSheetBehavior.isHideable = true
                bottomSheetBehavior.isDraggable = false
            }
        }
        bottomSheetBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                //
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //
            }
        })
    }

    private fun goToDetailLink(url: String, bottomSheetDetailBinding: BottomSheetDetailBinding) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        bottomSheetDetailBinding.btnGoDetail.setOnClickListener {
            startActivity(intent)
        }
    }
}