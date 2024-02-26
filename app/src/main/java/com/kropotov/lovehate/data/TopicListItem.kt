package com.kropotov.lovehate.data

data class TopicListItem(
    val title: String,
    val isLoved: Boolean,
    val commentsCount: Int,
    val feelingPercent: Int
)
