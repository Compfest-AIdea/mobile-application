package com.compfest.aiapplication.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PredictionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertResult(predictionResult: PredictionResult)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInputTabularData(predictionTabularInput: PredictionTabularInput)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInputImageData(predictionImageInput: PredictionImageInput)
}