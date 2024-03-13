package com.kropotov.lovehate.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.fragment.OpinionListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OpinionsRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {
    fun getOpinionsStream(opinionSortType: OpinionSortType): Flow<PagingData<OpinionListItem>> = Pager(
        config = PagingConfig(enablePlaceholders = true, pageSize = OPINIONS_PAGE_SIZE),
        pagingSourceFactory = { OpinionsPagingSource(apolloClient, opinionSortType) }
    ).flow

    private companion object {
        const val OPINIONS_PAGE_SIZE = 10
    }
}