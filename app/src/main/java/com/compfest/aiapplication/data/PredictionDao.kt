package com.compfest.aiapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface PredictionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPredictionTabularInput(predictionTabularInput: PredictionTabularInput)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPredictionImageInput(predictionImageInput: PredictionImageInput)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPredictionTabularResult(prediction: PredictionTabularResult)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPredictionImageResult(prediction: PredictionImageResult)
}