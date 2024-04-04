package com.kropotov.lovehate.ui.screens.topics

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.repositories.TopicsRepository
import com.kropotov.lovehate.data.TopicType
import com.kropotov.lovehate.fragment.TopicListItem
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TopicsViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    private val sortType: TopicType,
    private val repository: TopicsRepository,
    val separateToolbar: MyTopicsToolbar
) : BaseViewModel(resourceProvider) {

    override val noDataTextRes: Int = R.string.no_topics_was_found

    init {
        separateToolbar.toolbarVisibility.set(sortType == TopicType.BY_CURRENT_USER)
    }

    fun searchTopics(queryString: String): Flow<PagingData<TopicListItem>> =
        repository.getTopicsStream(queryString, sortType).cachedIn(viewModelScope)
}
