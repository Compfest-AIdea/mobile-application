package com.compfest.aiapplication.data.nestedItem

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FaQ(
    @SerialName("q")
    val question: String,
    @SerialName("a")
    val answers: List<String>
)