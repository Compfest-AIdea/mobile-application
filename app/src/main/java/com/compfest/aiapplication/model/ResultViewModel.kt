package com.compfest.aiapplication.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.compfest.aiapplication.data.PredictionImageInput
import com.compfest.aiapplication.data.PredictionTabularInput
import com.compfest.aiapplication.data.PredictionImageResult
import com.compfest.aiapplication.data.PredictionRepository
import com.compfest.aiapplication.data.PredictionResult
import com.compfest.aiapplication.data.PredictionTabularResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResultViewModel(private val repository: PredictionRepository): ViewModel() {
    fun saveResult(predictionResult: PredictionResult) {
        repository.insertResult(predictionResult)
    }

    fun saveInputData(predictionTabularInput: PredictionTabularInput, predictionImageInput: PredictionImageInput) {
        repository.insertPredictionInputData(predictionTabularInput, predictionImageInput)
    }

    fun getPredictionResult(id: Int): LiveData<PredictionResult> {
        val result = liveData {
            val predictionResult = withContext(Dispatchers.IO) {
                repository.getPredictionResultById(id)
            }
            emit(predictionResult)
        }
        return result
    }
}