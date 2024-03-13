package com.kropotov.lovehate.ui.utilities

import androidx.databinding.ObservableField
import com.kropotov.lovehate.R
import com.kropotov.lovehate.fragment.OpinionListItem

class LikeDislikeChecker(isLiked: Boolean, isDisliked: Boolean, likeCount: Int, dislikeCount: Int) {

    constructor(opinion: OpinionListItem) : this(
        opinion.isLiked,
        opinion.isDisliked,
        opinion.likeCount,
        opinion.dislikeCount
    )

    private var isLiked: ObservableField<Boolean> = ObservableField(isLiked)
    private var isDisliked: ObservableField<Boolean> = ObservableField(isDisliked)
    var likeCount: ObservableField<String> = ObservableField("${likeCount}")
    var dislikeCount: ObservableField<String> = ObservableField("${dislikeCount}")

    val likeIcon = ObservableField(R.string.icon_like)
    val likeBackground = ObservableField(R.drawable.reaction_background)

    val dislikeIcon = ObservableField(R.string.icon_dislike)
    val dislikeBackground = ObservableField(R.drawable.reaction_background)

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