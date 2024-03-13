package com.kropotov.lovehate.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.api.BackendQueryAdapter
import com.kropotov.lovehate.fragment.OpinionListItem

class OpinionsPagingSource(
    private val apolloClient: ApolloClient,
    private val opinionSortType: OpinionSortType
) : PagingSource<Int, OpinionListItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OpinionListItem> {
        val page = params.key ?: 0
        return try {
            val withoutUnionType = if (opinionSortType == OpinionSortType.UNION) null else opinionSortType
            val response = apolloClient
                .query(BackendQueryAdapter.latestOpinions(null, withoutUnionType, page))
                .execute()
                .data?.latestOpinions

            LoadResult.Page(
                data = response?.results?.map { it.opinionListItem } ?: listOf(),
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response == null || page == response.totalPages) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, OpinionListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}