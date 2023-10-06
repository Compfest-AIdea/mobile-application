package com.compfest.aiapplication.data.nestedItem

data class FaqItem(
    val question: String,
    val sublist: List<String>,
    var isExpanded: Boolean = false
)
