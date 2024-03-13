package com.kropotov.lovehate.ui.viewmodels.feed

import androidx.databinding.ObservableField
import com.kropotov.lovehate.R
import com.kropotov.lovehate.fragment.OpinionListItem
import com.kropotov.lovehate.type.OpinionType
import com.kropotov.lovehate.ui.utilities.Favorite
import com.kropotov.lovehate.ui.utilities.LikeDislikeChecker

class OpinionViewModel(val opinion: OpinionListItem): Favorite {

    val username: String
        get() = opinion.username

    val date: String
        get() = opinion.date

    val type: OpinionType
        get() = opinion.type

    val topic: String
        get() = opinion.topicTitle

    val text: String
        get() = opinion.text

    val messagesCount: Int
        get() = opinion.messagesCount

    val opinionTypeFormatted: Int
        get() = when(type) {
            OpinionType.LOVE -> R.string.opinion_loves
            OpinionType.HATE -> R.string.opinion_hate
            else -> R.string.opinion_neutral
        }

    val opinionColor = when (type) {
        OpinionType.LOVE -> R.attr.love_color
        OpinionType.HATE -> R.attr.hate_color
        else -> R.attr.unaccented_text_color
    }

    val likeDislikeChecker = LikeDislikeChecker(opinion)

    override var isFavorite = opinion.isFavorite

    override var favoriteIcon = ObservableField(R.string.icon_favorite)

    override var favoriteIconColor = ObservableField(R.attr.unaccented_text_color)

    init {
        updateFavoriteIcon()
    }
}