package com.kropotov.lovehate.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.api.BackendQueryAdapter
import com.kropotov.lovehate.fragment.TopicListItem
import com.kropotov.lovehate.type.TopicsSortType as GeneratedTopicsSortType

class TopicsPagingSource(
    private val apolloClient: ApolloClient,
    private val topicsSortType: TopicsSortType
) : PagingSource<Int, TopicListItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TopicListItem> {
        val page = params.key ?: 0
        return try {
            val sortType = GeneratedTopicsSortType.valueOf(topicsSortType.name)
            val response = apolloClient
                .query(BackendQueryAdapter.getTopics(sortType, page))
                .execute()
                .data?.topics

            LoadResult.Page(
                data = response?.results?.map { it.topicListItem } ?: listOf(),
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response == null || page == response.totalPages) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TopicListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}