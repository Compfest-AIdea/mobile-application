package com.compfest.aiapplication.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
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

    @Query("SELECT * FROM predictionresult")
    fun getAllPredictionResult(): DataSource.Factory<Int, PredictionResult>

    @Query("SELECT * FROM predictionresult WHERE id= :id")
    fun getPredictionResultById(id: Int): PredictionResult

    @Query("SELECT * FROM predictionresult ORDER BY id ASC LIMIT 1")
    fun getRecentPredictionResult(): LiveData<PredictionResult?>
}