package com.kropotov.lovehate.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.data.pagingsources.MyRatingPagingSource
import com.kropotov.lovehate.data.pagingsources.OpinionsPagingSource
import com.kropotov.lovehate.di.MainActivityScope
import com.kropotov.lovehate.fragment.MyRatedEvent
import com.kropotov.lovehate.fragment.OpinionListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@MainActivityScope
class MyRatingRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {

    fun getMyRatingItems(): Flow<PagingData<MyRatedEvent>> = Pager(
        config = PagingConfig(enablePlaceholders = true, pageSize = MY_RATING_PAGE_SIZE),
        pagingSourceFactory = { MyRatingPagingSource(apolloClient) }
    ).flow

    companion object {
        const val MY_RATING_PAGE_SIZE = 15
    }
}