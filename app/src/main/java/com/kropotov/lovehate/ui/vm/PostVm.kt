package com.kropotov.lovehate.ui.vm

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.FeelingType

class PostVm(
    val username: String,
    val date: String,
    val feeling: FeelingType,
    val topic: String,
    val text: String,
    val feelingStr: String = "",
    var isFavorite: Boolean = false,
    var isLiked: ObservableField<Boolean> = ObservableField(false),
    var isDisliked: ObservableField<Boolean> = ObservableField(false),
    var likeCount: ObservableField<String> = ObservableField("0"),
    var dislikeCount: ObservableField<String> = ObservableField("0"),
    val messagesCount: Int = 0
): ViewModel() {

    var favoriteIcon = ObservableField(R.string.icon_favorite)
    var favoriteIconColor = ObservableField(R.attr.unaccented_text_color)

    val feelingColor = when (feeling) {
        FeelingType.LOVE -> R.attr.love_color
        FeelingType.HATE -> R.attr.hate_color
        else -> R.attr.unaccented_text_color
    }

    val likeIcon = ObservableField(R.string.icon_like)
    val likeBackground = ObservableField(R.drawable.reaction_background)

    val dislikeIcon = ObservableField(R.string.icon_dislike)
    val dislikeBackground = ObservableField(R.drawable.reaction_background)

    fun onFavoriteClick() {
        isFavorite = !isFavorite
        if (isFavorite) {
            favoriteIcon.set(R.string.icon_favorite_filled)
            favoriteIconColor.set(R.attr.favorite_icon_color)
        } else {
            favoriteIcon.set(R.string.icon_favorite)
            favoriteIconColor.set(R.attr.unaccented_text_color)
        }
    }

    fun onLikeClick() {
        updateReaction(isLiked, likeCount)
        updateLikeIcon()
        if (isDisliked.get()!!) {
            updateReaction(isDisliked, dislikeCount)
            updateDislikeIcon()
        }
    }

    fun onDislikeClick() {
        updateReaction(isDisliked, dislikeCount)
        updateDislikeIcon()
        if (isLiked.get()!!) {
            updateReaction(isLiked, likeCount)
            updateLikeIcon()
        }
    }

    private fun updateLikeIcon() {
        likeIcon.set(if (isLiked.get()!!) R.string.icon_like_filled else R.string.icon_like)
        updateBackground(isLiked, likeBackground)
    }

    private fun updateDislikeIcon() {
        dislikeIcon.set(if (isDisliked.get()!!) R.string.icon_dislike_filled else R.string.icon_dislike)
        updateBackground(isDisliked, dislikeBackground)
    }

    private fun updateBackground(reactionState: ObservableField<Boolean>, reactionBackground: ObservableField<Int>) {
        val drawableRes = if (reactionState.get()!!) {
            R.drawable.reaction_background_selected
        } else {
            R.drawable.reaction_background
        }
        reactionBackground.set(drawableRes)
    }

    private fun updateReaction(reaction: ObservableField<Boolean>, reactionCount: ObservableField<String>) {
        val count = reactionCount.get()!!.toInt() + if (reaction.get()!!) -1 else +1
        reactionCount.set(count.toString())

        reaction.set(!reaction.get()!!)
    }
}