package com.compfest.aiapplication.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.compfest.aiapplication.data.PredictionTabularInput
import com.compfest.aiapplication.data.PredictionImageResult
import com.compfest.aiapplication.data.PredictionRepository
import com.compfest.aiapplication.data.PredictionTabularResult

class ResultViewModel(private val repository: PredictionRepository): ViewModel() {
    fun savePredictionInput(predictionTabularInput: PredictionTabularInput) {
        repository.insertPredictionTabularInput(predictionTabularInput)
    }

    fun savePredictionTabularResult(prediction: PredictionTabularResult) {
        repository.insertPredictionTabularResult(prediction)
    }

    fun savePredictionImageResult(prediction: PredictionImageResult) {
        repository.insertPredictionImageResult(prediction)
    }

}