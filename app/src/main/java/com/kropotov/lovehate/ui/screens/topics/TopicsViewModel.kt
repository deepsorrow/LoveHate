package com.kropotov.lovehate.ui.screens.topics

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kropotov.lovehate.data.repositories.TopicsRepository
import com.kropotov.lovehate.data.TopicType
import com.kropotov.lovehate.fragment.TopicListItem
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TopicsViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    repository: TopicsRepository,
    sortType: TopicType,
    val myTopicsToolbar: MyTopicsToolbar
) : BaseViewModel(resourceProvider) {

    var items: Flow<PagingData<TopicListItem>> =
        repository.getTopicsStream(sortType).cachedIn(viewModelScope)

    init {
        myTopicsToolbar.toolbarVisibility.set(sortType == TopicType.BY_CURRENT_USER)
    }
}
