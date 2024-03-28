package com.kropotov.lovehate.data.items

import com.kropotov.lovehate.R
import com.kropotov.lovehate.type.OpinionsListType
import java.lang.IllegalArgumentException

data class OpinionRatingListItem(
    override val ratingType: OpinionsListType,
    override var subtitle: String? = null,
    override val action: (() -> Unit)? = null
): RatingListItem(subtitle, action) {

    override val title = when (ratingType) {
        OpinionsListType.MOST_LIKED -> R.string.most_liked
        OpinionsListType.MOST_DISLIKED -> R.string.most_disliked
        else -> throw IllegalArgumentException("No such opinion rating list type!")
    }
}
