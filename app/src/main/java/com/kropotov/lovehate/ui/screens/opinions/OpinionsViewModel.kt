package com.kropotov.lovehate.ui.screens.opinions

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kropotov.lovehate.R
import com.kropotov.lovehate.analytics.Analytics
import com.kropotov.lovehate.analytics.AnalyticsEvent
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.data.repositories.OpinionsRepository
import com.kropotov.lovehate.fragment.OpinionListItem
import com.kropotov.lovehate.type.OpinionsListType
import com.kropotov.lovehate.type.OpinionsListType.BY_CURRENT_USER
import com.kropotov.lovehate.type.OpinionsListType.MOST_LIKED
import com.kropotov.lovehate.type.OpinionsListType.MOST_DISLIKED
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.screens.opinions.fragments.OpinionsFragment.Companion.OPINIONS_FILTER_TYPE_ARG
import com.kropotov.lovehate.ui.screens.opinions.fragments.OpinionsFragment.Companion.OPINIONS_TOPIC_ID_ARG
import com.kropotov.lovehate.ui.screens.opinions.fragments.OpinionsFragment.Companion.OPINIONS_SORT_TYPE_ARG
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class OpinionsViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    @Named(OPINIONS_TOPIC_ID_ARG) private val topicId: Int?,
    @Named(OPINIONS_SORT_TYPE_ARG) private val sortType: OpinionType,
    @Named(OPINIONS_FILTER_TYPE_ARG) private val listType: OpinionsListType,
    private val analytics: Analytics,
    val repository: OpinionsRepository,
    val separateToolbar: OpinionsToolbar
) : BaseViewModel(resourceProvider) {

    private var currentSearchQuery: String? = null

    override val noDataTextRes: Int =
        if (topicId == null || currentSearchQuery != null) {
            R.string.no_opinions_was_found
        } else when {
            (sortType == OpinionType.LOVE) -> R.string.no_love_opinions_here_yet
            (sortType == OpinionType.HATE) -> R.string.no_hate_opinions_here_yet
            (sortType == OpinionType.INDIFFERENCE) -> R.string.no_neutral_opinions_here_yet
            else -> R.string.no_opinions_was_found
        }

    private val isWithoutHostScreen =
        listType == BY_CURRENT_USER || listType == MOST_LIKED || listType == MOST_DISLIKED

    val isRatingScreen = listType == MOST_LIKED || listType == MOST_DISLIKED

    val backgroundColor = if (isWithoutHostScreen) {
        R.attr.union_background_color
    } else {
        R.attr.transparent_color
    }

    init {
        if (isWithoutHostScreen) {
            val title = when (listType) {
                MOST_LIKED -> R.string.most_liked
                MOST_DISLIKED -> R.string.most_disliked
                else -> R.string.my_opinions
            }

            separateToolbar.title.set(title)
        } else {
            separateToolbar.toolbarVisibility.set(false)
        }

        if (isRatingScreen) {
            separateToolbar.titleTextSize.set(R.dimen.toolbar_subtitle_text_size)
        }
        sendAnalytics()
    }

    fun searchOpinions(queryString: String): Flow<PagingData<OpinionListItem>> {
        if (queryString.isNotEmpty()) {
            analytics.send(AnalyticsEvent.SearchOpinions(queryString))
        }

        return repository.getOpinionsStream(queryString, topicId, sortType, listType)
            .cachedIn(viewModelScope)
    }

    private fun sendAnalytics() {
        val event = AnalyticsEvent.ShowOpinionsList(listType.name, topicId)
        analytics.send(event)
    }
}
