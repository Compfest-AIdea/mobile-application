package com.compfest.aiapplication.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.compfest.aiapplication.data.PredictionImageInput
import com.compfest.aiapplication.data.PredictionTabularInput
import com.compfest.aiapplication.data.PredictionImageResult
import com.compfest.aiapplication.data.PredictionRepository
import com.compfest.aiapplication.data.PredictionResult
import com.compfest.aiapplication.data.PredictionTabularResult

class ResultViewModel(private val repository: PredictionRepository): ViewModel() {
    fun saveResult(predictionResult: PredictionResult) {
        repository.insertResult(predictionResult)
    }

    fun saveInputData(predictionTabularInput: PredictionTabularInput, predictionImageInput: PredictionImageInput) {
        repository.insertPredictionInputData(predictionTabularInput, predictionImageInput)
    }
}