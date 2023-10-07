package com.compfest.aiapplication.data.nestedItem

import kotlinx.serialization.SerialName

data class DetailItem(
    val title: String,
    val description: Description,
    var isExpanded: Boolean = false,
)
