package com.compfest.aiapplication.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PredictionDao {

    @Query("SELECT * FROM Prediction WHERE id= :predictionId")
    fun getImageById(predictionId: Int): LiveData<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPrediction(prediction: Prediction)
}