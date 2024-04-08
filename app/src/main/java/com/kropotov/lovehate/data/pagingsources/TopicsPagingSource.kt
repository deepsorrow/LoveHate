package com.kropotov.lovehate.data.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.api.main.TopicsQueryAdapter
import com.kropotov.lovehate.data.TopicType
import com.kropotov.lovehate.data.repositories.TopicsRepository.Companion.TOPICS_PAGE_SIZE
import com.kropotov.lovehate.fragment.TopicListItem
import com.kropotov.lovehate.type.TopicsListType

class TopicsPagingSource(
    private val apolloClient: ApolloClient,
    private val searchQuery: String,
    private val sortType: TopicType
) : PagingSource<Int, TopicListItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TopicListItem> {
        val page = params.key ?: 0
        return try {
            val sortType = TopicsListType.valueOf(sortType.name)
            val response = apolloClient
                .query(TopicsQueryAdapter.getTopics(sortType, searchQuery, page))
                .execute()
                .dataAssertNoErrors
                .topics

            LoadResult.Page(
                data = response.results.map { it.topicListItem },
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (page == response.totalPages) null else page + 1,
                itemsBefore = 0,
                itemsAfter = (response.totalPages - page) * TOPICS_PAGE_SIZE
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TopicListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}
