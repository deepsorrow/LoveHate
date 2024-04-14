package com.kropotov.lovehate.data.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.api.main.MyRatingQueryAdapter
import com.kropotov.lovehate.data.repositories.MyRatingRepository.Companion.MY_RATING_PAGE_SIZE
import com.kropotov.lovehate.fragment.MyRatedEvent

class MyRatingPagingSource(
    private val apolloClient: ApolloClient
) : PagingSource<Int, MyRatedEvent>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MyRatedEvent> {
        val page = params.key ?: 0
        return try {
            val response = apolloClient
                .query(MyRatingQueryAdapter.getLatestItems(page))
                .execute()
                .dataAssertNoErrors
                .myRatedEvents

            LoadResult.Page(
                data = response.results.map { it.myRatedEvent },
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (page == response.totalPages) null else page + 1,
                itemsBefore = 0,
                itemsAfter = (response.totalPages - page) * MY_RATING_PAGE_SIZE
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MyRatedEvent>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}