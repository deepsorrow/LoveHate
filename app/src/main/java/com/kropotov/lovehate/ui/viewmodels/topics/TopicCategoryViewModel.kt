package com.kropotov.lovehate.ui.viewmodels.topics

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kropotov.lovehate.data.TopicsRepository
import com.kropotov.lovehate.data.TopicsSortType
import com.kropotov.lovehate.fragment.TopicListItem
import com.kropotov.lovehate.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TopicCategoryViewModel @Inject constructor(
    private val repository: TopicsRepository,
    private val sortType: TopicsSortType
) : BaseViewModel() {

    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<TopicListItem>>? = null

    fun searchTopics(queryString: String): Flow<PagingData<TopicListItem>> {
        currentQueryValue = queryString
        val newResult: Flow<PagingData<TopicListItem>> =
            repository.getTopicsStream(sortType).cachedIn(viewModelScope)


        currentSearchResult = newResult
        return newResult
    }
}