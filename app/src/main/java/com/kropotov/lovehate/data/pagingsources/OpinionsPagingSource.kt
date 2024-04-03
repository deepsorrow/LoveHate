package com.kropotov.lovehate.data.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.api.main.OpinionsQueryAdapter
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.fragment.OpinionListItem
import com.kropotov.lovehate.type.OpinionsListType

class OpinionsPagingSource(
    private val apolloClient: ApolloClient,
    private val searchQuery: String,
    private val topicId: Int?,
    private val sortType: OpinionType,
    private val listType: OpinionsListType
) : PagingSource<Int, OpinionListItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OpinionListItem> {
        val page = params.key ?: 0
        return try {
            val query = if (listType == OpinionsListType.ALL) {
                OpinionsQueryAdapter.getLatestOpinions(topicId, sortType, searchQuery, page)
            } else {
                OpinionsQueryAdapter.getOpinions(false, listType, searchQuery, page)
            }
            val response = apolloClient
                .query(query)
                .execute()
                .dataAssertNoErrors
                .opinions

            LoadResult.Page(
                data = response.results.map { it.opinionListItem },
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (page == response.totalPages) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, OpinionListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}
