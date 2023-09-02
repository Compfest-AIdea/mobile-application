package com.compfest.aiapplication.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class PredictionTabularInput(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
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
): Parcelable

@Parcelize
@Entity
data class PredictionTabularResult(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "class1")
    val class1: Float,

    @ColumnInfo(name = "class2")
    val class2: Float,

    @ColumnInfo(name = "class3")
    val class3: Float,

    @ColumnInfo(name = "class4")
    val class4: Float,
): Parcelable

@Entity
@Parcelize
data class PredictionImageInput(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "imagePath")
    val imagePath: String
): Parcelable

@Parcelize
@Entity
data class PredictionImageResult(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "class1")
    val class1: Float,

    @ColumnInfo(name = "class2")
    val class2: Float,

    @ColumnInfo(name = "class3")
    val class3: Float,

    @ColumnInfo(name = "class4")
    val class4: Float,

    @ColumnInfo(name = "class5")
    val class5: Float
): Parcelable

@Parcelize
@Entity
data class PredictionResult(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "resultHairLoss")
    val resultHairLoss: String,

    @ColumnInfo(name = "resultScalpCondi")
    val resultScalpCondi: String,

    @ColumnInfo(name = "imgPath")
    val imgPath: String
): Parcelable
