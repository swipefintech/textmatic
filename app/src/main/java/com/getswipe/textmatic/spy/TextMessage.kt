package com.getswipe.textmatic.spy

import kotlinx.serialization.Serializable

@Serializable
data class TextMessage(
    val direction: String,
    val participant: String,
    val content: String,
    val date: Long
)
