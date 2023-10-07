package com.compfest.aiapplication.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.compfest.aiapplication.R
import com.compfest.aiapplication.data.PredictionImageInput
import com.compfest.aiapplication.data.PredictionImageResult
import com.compfest.aiapplication.data.PredictionResult
import com.compfest.aiapplication.data.PredictionTabularInput
import com.compfest.aiapplication.data.PredictionTabularResult
import com.compfest.aiapplication.data.nestedItem.Detail
import com.compfest.aiapplication.databinding.ActivityResultBinding
import com.compfest.aiapplication.databinding.BottomSheetDetailBinding
import com.compfest.aiapplication.getCurrentTimeMillis
import com.compfest.aiapplication.getImageFromExternalStorage
import com.compfest.aiapplication.loadDataFromJson
import com.compfest.aiapplication.model.ResultViewModel
import com.compfest.aiapplication.model.ViewModelFactory
import com.compfest.aiapplication.ui.adapter.nestedAdapter.detail.ExtendedDetailRecyclerViewAdapter
import com.compfest.aiapplication.ui.fragment.HomeFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.serialization.json.Json

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var viewModel: ResultViewModel
    private lateinit var bottomSheetBinding: BottomSheetDetailBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

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
        return when (maxValIndex) {
            3 -> {
                "Serious Hair Loss"
            }

            2 -> {
                "Hair Loss"
            }

            1 -> {
                "Slight Hair Loss"
            }

            else -> {
                "Normal"
            }
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
        return when (maxValIndex) {
            4 -> {
                disesaseName[0]
            }

            3 -> {
                disesaseName[1]
            }

            2 -> {
                disesaseName[2]
            }

            1 -> {
                "normal"
            }

            else -> {
                disesaseName[3]
            }
        }
    }

    private fun setBottomSheetDetail(imageResult: String) {
        val bottomSheetDetail: View = findViewById(R.id.bottom_sheet)
        bottomSheetBinding = BottomSheetDetailBinding.bind(bottomSheetDetail)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetDetail)

        val childRVavoid = bottomSheetBinding.rvDiseaseExtendedDetail0
        childRVavoid.layoutManager = LinearLayoutManager(this)
        val childRVtreat = bottomSheetBinding.rvDiseaseExtendedDetail1
        childRVtreat.layoutManager = LinearLayoutManager(this)
        val detailData: List<Detail> = Json.decodeFromString(loadDataFromJson("detail_article.json", this))

        val diseaseName = resources.getStringArray(R.array.disease_name)
        val diseaseDesc = resources.getStringArray(R.array.disease_desc)
        when (imageResult) {
            diseaseName[0] -> {
                bottomSheetBinding.tvDiseaseArticleTitle.text = diseaseName[0]
                bottomSheetBinding.tvDisesaseArticleDesc.text = diseaseDesc[0]
                showDetail()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                childRVavoid.adapter = ExtendedDetailRecyclerViewAdapter(detailData[0].description.pencegahan)
                childRVtreat.adapter = ExtendedDetailRecyclerViewAdapter(detailData[0].description.pengobatan)
            }
            diseaseName[1] -> {
                bottomSheetBinding.tvDiseaseArticleTitle.text = diseaseName[1]
                bottomSheetBinding.tvDisesaseArticleDesc.text = diseaseDesc[1]
                showDetail()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                childRVavoid.adapter = ExtendedDetailRecyclerViewAdapter(detailData[1].description.pencegahan)
                childRVtreat.adapter = ExtendedDetailRecyclerViewAdapter(detailData[1].description.pengobatan)
            }
            diseaseName[2] -> {
                bottomSheetBinding.tvDiseaseArticleTitle.text = diseaseName[2]
                bottomSheetBinding.tvDisesaseArticleDesc.text = diseaseDesc[2]
                showDetail()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                childRVavoid.adapter = ExtendedDetailRecyclerViewAdapter(detailData[2].description.pencegahan)
                childRVtreat.adapter = ExtendedDetailRecyclerViewAdapter(detailData[2].description.pengobatan)
            }
            diseaseName[3] -> {
                bottomSheetBinding.tvDiseaseArticleTitle.text = diseaseName[3]
                bottomSheetBinding.tvDisesaseArticleDesc.text = diseaseDesc[3]
                showDetail()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                childRVavoid.adapter = ExtendedDetailRecyclerViewAdapter(detailData[3].description.pencegahan)
                childRVtreat.adapter = ExtendedDetailRecyclerViewAdapter(detailData[3].description.pengobatan)
            }
            else -> {
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.isDraggable = true
                bottomSheetBinding.tvDiseaseArticleSource.text = "Sumber: Bestlife.com"
                bottomSheetBinding.tvDiseaseArticleTitle.text = "Tips perawatan kulit kepala normal"
                bottomSheetBinding.tvDisesaseArticleDesc.text = detailData[4].description.pencegahan[0]
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBinding.btnGoDetail.visibility = View.GONE
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

    private fun showDetail() {
        bottomSheetBinding.btnGoDetail.setOnClickListener {
            if (bottomSheetBinding.contentExtendedDetail.visibility == View.GONE) {
                bottomSheetBinding.contentExtendedDetail.visibility = View.VISIBLE
                bottomSheetBinding.btnGoDetail.text = "Sembunyikan Detail"
            } else {
                bottomSheetBinding.contentExtendedDetail.visibility = View.GONE
                bottomSheetBinding.btnGoDetail.text = "Lihat Detail"
            }
        }
    }
}