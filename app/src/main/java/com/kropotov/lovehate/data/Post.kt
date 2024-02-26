package com.kropotov.lovehate.data
data class Post(
    val username: String,
    val date: String,
    val topic: String,
    val opinion: FeelingType,
    val text: String,
    val likeCount: Int = 0,
    val dislikeCount: Int = 0,
    val messagesCount: Int = 0
)
