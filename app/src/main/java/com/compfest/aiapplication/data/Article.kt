package com.compfest.aiapplication.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val title: String,
    val story: String,
    val imagePath: Int
): Parcelable
