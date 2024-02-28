package com.kropotov.lovehate.data

data class Topic(
    val title: String,
    val isLoved: Boolean,
    val commentsCount: Int,
    val feelingPercent: Int
)
