package com.compfest.aiapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PredictionResult::class, PredictionTabularInput::class, PredictionImageInput::class], version = 1, exportSchema = false)
abstract class PredictionDatabase: RoomDatabase() {

    abstract fun predictionDao(): PredictionDao

    companion object {
        @Volatile
        private var INSTANCE: PredictionDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): PredictionDatabase {
            if (INSTANCE == null) {
                synchronized(PredictionDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        PredictionDatabase::class.java,
                        "prediction_db"
                    ).build()
                }
            }
            return INSTANCE as PredictionDatabase
        }
    }
}