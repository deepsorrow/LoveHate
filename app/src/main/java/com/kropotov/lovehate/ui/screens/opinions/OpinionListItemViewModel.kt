package com.kropotov.lovehate.ui.screens.opinions

import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.BR
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.InformType
import com.kropotov.lovehate.fragment.OpinionListItem as OpinionListItemGenerated
import com.kropotov.lovehate.type.OpinionType
import com.kropotov.lovehate.type.ReactionType
import com.kropotov.lovehate.ui.utilities.Favorite
import com.kropotov.lovehate.ui.utilities.extractErrorMessage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@OptIn(FlowPreview::class)
class OpinionListItemViewModel(
    private val opinion: OpinionListItemGenerated,
    private val viewModel: OpinionsViewModel
): BaseObservable(), Favorite, OnCheckedChangeListener {

    private val reactionFlow = MutableSharedFlow<ReactionType>()
    
    override var isFavorite = opinion.isFavorite
        set(value) {
            field = value
            updateFavoriteIcon()
        }
    override var isFavoriteFetching = false
    override var favoriteIcon = ObservableField(R.string.icon_favorite)

    override var favoriteIconColor = ObservableField(R.attr.unaccented_text_color)
    val id: Int get() = opinion.id
    val position = "# ${opinion.position}"
    val isPositionVisible = viewModel.isRatingScreen
    val username: String get() = opinion.username
    val date: String get() = opinion.date
    val type: OpinionType get() = opinion.type
    val topic: String get() = opinion.topicTitle

    val text: String get() = opinion.text

    val opinionTypeFormatted: Int
        get() = when (type) {
            OpinionType.LOVE -> R.string.opinion_loves
            OpinionType.HATE -> R.string.opinion_hate
            else -> R.string.opinion_neutral
        }

    val opinionColor = when (type) {
        OpinionType.LOVE -> R.attr.love_color
        OpinionType.HATE -> R.attr.hate_color
        else -> R.attr.unaccented_text_color
    }

    val isLikeChecked = opinion.isLiked

    val isDislikeChecked = opinion.isDisliked
    @get:Bindable
    var likeCount by Delegates.observable(opinion.likeCount) { _, _, _ ->
        notifyPropertyChanged(BR.likeCount)
    }
    @get:Bindable
    var dislikeCount by Delegates.observable(opinion.dislikeCount) { _, _, _ ->
        notifyPropertyChanged(BR.dislikeCount)
    }

    init {
        updateFavoriteIcon()
        subscribeToSendReaction()
    }

    override fun onFavoriteClick() {
        super.onFavoriteClick()

        isFavoriteFetching = true
        viewModel.run {
            val handler = CoroutineExceptionHandler { _, exception ->
                viewModelScope.launch {
                    val errorText = exception.extractErrorMessage()
                    emitMessage(R.string.unknown_error, InformType.ERROR, errorText)
                    isFavorite = !isFavorite
                }
            }
            viewModelScope.launch(Dispatchers.IO + handler) {
                isFavorite = repository.updateFavorite(id)
            }
        }
    }

    /**
     * Like/dislike checked listener.
     */
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        val itIsLikeChip = buttonView?.id == R.id.like_chip
        if (isChecked) {
            updateCheckedCount(itIsLikeChip)
        } else {
            updateUncheckedCount(itIsLikeChip)
        }

        viewModel.viewModelScope.launch {
            val reaction = if (itIsLikeChip) ReactionType.LIKE else ReactionType.DISLIKE
            reactionFlow.emit(reaction)
        }
    }

    private fun subscribeToSendReaction() {
        viewModel.run {
            viewModelScope.launch(Dispatchers.IO + defaultExceptionHandler) {
                reactionFlow.debounce(SEND_REACTION_DEBOUNCE).collect { reaction ->
                    repository.updateReaction(id, reaction)
                }
            }
        }
    }

    private fun updateCheckedCount(isLike: Boolean) {
        if (isLike) {
            likeCount = "${likeCount.toInt() + 1}"
        } else {
            dislikeCount = "${dislikeCount.toInt() + 1}"
        }
    }

    private fun updateUncheckedCount(isLike: Boolean) {
        if (isLike) {
            likeCount = "${likeCount.toInt() - 1}"
        } else {
            dislikeCount = "${dislikeCount.toInt() - 1}"
        }
    }

    private companion object {
        const val SEND_REACTION_DEBOUNCE = 500L
    }
}
