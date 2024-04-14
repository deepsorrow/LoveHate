package com.kropotov.lovehate.ui.screens.myrating

import com.kropotov.lovehate.R
import com.kropotov.lovehate.fragment.MyRatedEvent
import com.kropotov.lovehate.type.SourceType

class MyRatingListItemViewModel(
    private val item: MyRatedEvent
) {
    val textRes get() = when (item.sourceType) {
        SourceType.TOPIC -> R.string.icon_topics
        SourceType.OPINION -> R.string.icon_messages
        SourceType.DISLIKE -> R.string.icon_dislike
        else -> throw IllegalStateException("Unknown myRating source type!")
    }
    val date get() = item.date
    val text get() = item.text
    val points get() = item.points.toString()
    val isPositivePoints get() = item.points >= 0
}