package com.compfest.aiapplication.data

import android.content.Context
import androidx.lifecycle.LiveData
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class PredictionRepository(private val dao: PredictionDao) {

    private val executor: Executor = Executors.newSingleThreadExecutor()

    fun insertPrediction(prediction: Prediction) {
        executor.execute {
            dao.insertPrediction(prediction)
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