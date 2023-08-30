package com.compfest.aiapplication.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Prediction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "stayUpLate")
    val stayUpLate: Int,

    @ColumnInfo(name = "coffeeConsumption")
    val coffeeConsumption: Int,

    @ColumnInfo(name = "brainWorkingDuration")
    val brainWorkingDuration: Int,

    @ColumnInfo(name = "pressureLevel")
    val pressureLevel: Int,

    @ColumnInfo(name = "stressLevel")
    val stressLevel: Int,

    @ColumnInfo(name = "swimming")
    val swimming: Int,

    @ColumnInfo(name = "hairWashing")
    val hairWashing: Int,

    @ColumnInfo(name = "dandruff")
    val dandruff: Int,

    @ColumnInfo(name = "imagePath")
    val imagePath: ImagePath? = null
): Parcelable

@Parcelize
data class ImagePath(
    val imagePath1: String? = null,
    val imagePath2: String? = null,
    val imagePath3: String? = null,
    val imagePath4: String? = null,
    val imagePath5: String? = null
): Parcelable
