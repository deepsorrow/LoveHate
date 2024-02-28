package com.kropotov.lovehate.data

data class TopicMetadata(
    val date: String,
    val author: String,
    val isFavorite: Boolean,
    val authorOpinionType: OpinionType,
    val similarTopics: List<Topic>
)