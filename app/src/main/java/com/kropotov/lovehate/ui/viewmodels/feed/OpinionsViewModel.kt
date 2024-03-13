package com.kropotov.lovehate.ui.viewmodels.feed

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kropotov.lovehate.data.OpinionSortType
import com.kropotov.lovehate.data.OpinionsRepository
import com.kropotov.lovehate.fragment.OpinionListItem
import com.kropotov.lovehate.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OpinionsViewModel @Inject constructor(
    private val repository: OpinionsRepository,
    private val sortType: OpinionSortType
) : BaseViewModel() {

    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<OpinionListItem>>? = null

    fun searchOpinions(queryString: String): Flow<PagingData<OpinionListItem>> {
        currentQueryValue = queryString
        val newResult: Flow<PagingData<OpinionListItem>> =
            repository.getOpinionsStream(sortType).cachedIn(viewModelScope)


        currentSearchResult = newResult
        return newResult
    }
}