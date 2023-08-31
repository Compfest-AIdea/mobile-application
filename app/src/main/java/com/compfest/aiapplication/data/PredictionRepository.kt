package com.compfest.aiapplication.data

import android.content.Context
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class PredictionRepository(private val dao: PredictionDao) {

    private val executor: Executor = Executors.newSingleThreadExecutor()

    fun insertPredictionTabularInput(predictionTabularInput: PredictionTabularInput) {
        executor.execute {
            dao.insertPredictionTabularInput(predictionTabularInput)
        }
    }

    fun insertPredictionImageInput(predictionImageInput: PredictionImageInput) {
        executor.execute {
            dao.insertPredictionImageInput(predictionImageInput)
        }
    }

    fun insertPredictionTabularResult(prediction: PredictionTabularResult) {
        executor.execute {
            dao.insertPredictionTabularResult(prediction)
        }
    }

    fun insertPredictionImageResult(prediction: PredictionImageResult) {
        executor.execute {
            dao.insertPredictionImageResult(prediction)
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