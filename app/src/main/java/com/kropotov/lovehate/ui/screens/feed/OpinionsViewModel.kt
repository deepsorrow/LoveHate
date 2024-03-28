package com.kropotov.lovehate.ui.screens.feed

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.data.repositories.OpinionsRepository
import com.kropotov.lovehate.fragment.OpinionListItem
import com.kropotov.lovehate.type.OpinionsListType
import com.kropotov.lovehate.type.OpinionsListType.BY_CURRENT_USER
import com.kropotov.lovehate.type.OpinionsListType.MOST_LIKED
import com.kropotov.lovehate.type.OpinionsListType.MOST_DISLIKED
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.screens.feed.fragments.OpinionsFragment.Companion.OPINIONS_FILTER_TYPE_ARG
import com.kropotov.lovehate.ui.screens.feed.fragments.OpinionsFragment.Companion.OPINIONS_TOPIC_ID_ARG
import com.kropotov.lovehate.ui.screens.feed.fragments.OpinionsFragment.Companion.OPINIONS_SORT_TYPE_ARG
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class OpinionsViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    @Named(OPINIONS_TOPIC_ID_ARG) private val topicId: Int?,
    @Named(OPINIONS_SORT_TYPE_ARG) private val sortType: OpinionType,
    @Named(OPINIONS_FILTER_TYPE_ARG) private val listType: OpinionsListType,
    val repository: OpinionsRepository,
    val separateToolbar: OpinionsToolbar
) : BaseViewModel(resourceProvider) {

    private val isSeparateScreen =
        listType == BY_CURRENT_USER || listType == MOST_LIKED || listType == MOST_DISLIKED

    var items: Flow<PagingData<OpinionListItem>> =
        repository.getOpinionsStream(topicId, sortType, listType).cachedIn(viewModelScope)

    val isRatingScreen = listType == MOST_LIKED || listType == MOST_DISLIKED

    val backgroundColor = if (isSeparateScreen) {
        R.attr.union_background_color
    } else {
        R.attr.transparent_color
    }

    init {
        if (isSeparateScreen) {
            separateToolbar.toolbarVisibility.set(true)
            val title = when (listType) {
                MOST_LIKED -> R.string.most_liked
                MOST_DISLIKED -> R.string.most_disliked
                else -> R.string.my_opinions
            }

            separateToolbar.title.set(title)
        }
    }
}
