package com.compfest.aiapplication.data

import androidx.lifecycle.LiveData
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class PredictionRepository(private val dao: PredictionDao) {

    private val executor: Executor = Executors.newSingleThreadExecutor()

    fun getImagesById(imageId: Int): LiveData<String> {
        return dao.getImageById(imageId)
    }

    fun insertPrediction(prediction: Prediction) {
        executor.execute {
            dao.insertPrediction(prediction)
        }
    }
}