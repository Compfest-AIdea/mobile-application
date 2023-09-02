package com.compfest.aiapplication.data

import android.content.Context
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class PredictionRepository(private val dao: PredictionDao) {

    private val executor: Executor = Executors.newSingleThreadExecutor()

    fun insertResult(predictionResult: PredictionResult) {
        executor.execute {
            dao.insertResult(predictionResult)
        }
    }

    fun insertPredictionInputData(predictionTabularInput: PredictionTabularInput, predictionImageInput: PredictionImageInput) {
        executor.execute {
            dao.insertInputTabularData(predictionTabularInput)
            dao.insertInputImageData(predictionImageInput)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: PredictionRepository? = null

        fun getInstance(context: Context): PredictionRepository {
            return INSTANCE ?: synchronized(this) {
                if (INSTANCE == null) {
                    val database = PredictionDatabase.getInstance(context)
                    INSTANCE = PredictionRepository(database.predictionDao())
                }
                return INSTANCE as PredictionRepository
            }

        }
    }
}