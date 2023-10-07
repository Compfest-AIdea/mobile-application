package com.compfest.aiapplication.data.nestedItem

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Detail(
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: Description
)

@Serializable
data class Description(
    @SerialName("pencegahan")
    val pencegahan: List<String>,
    @SerialName("pengobatan")
    val pengobatan: List<String>
)
