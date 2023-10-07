package com.compfest.aiapplication.data.nestedItem

data class DetailItem(
    val title: String,
    val description: Description,
    var isExpanded: Boolean = false,
)
