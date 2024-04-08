package com.kropotov.lovehate.data.repositories

import androidx.annotation.WorkerThread
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.CreateTopicMutation
import com.kropotov.lovehate.api.main.TopicsQueryAdapter
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.data.items.TopicListItem
import com.kropotov.lovehate.data.pagingsources.TopicsPagingSource
import com.kropotov.lovehate.data.TopicType
import com.kropotov.lovehate.di.MainActivityScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@MainActivityScope
class TopicsRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {
    fun getTopicsStream(
        searchQuery: String,
        topicType: TopicType
    ): Flow<PagingData<com.kropotov.lovehate.fragment.TopicListItem>> = Pager(
        config = PagingConfig(enablePlaceholders = true, pageSize = TOPICS_PAGE_SIZE),
        pagingSourceFactory = { TopicsPagingSource(apolloClient, searchQuery, topicType) }
    ).flow

    @WorkerThread
    suspend fun createTopic(title: String, opinionType: OpinionType, comment: String) =
        apolloClient.mutation(TopicsQueryAdapter.createTopic(title, opinionType, comment))
            .execute()
            .dataAssertNoErrors
            .addTopic

    @WorkerThread
    suspend fun getSimilarTopics(topicId: Int) =
        apolloClient.query(TopicsQueryAdapter.getSimilarTopics(topicId))
            .execute()
            .dataAssertNoErrors
            .similarTopics
            .map { topic -> TopicListItem(topic.topicListItem) }

    @WorkerThread
    suspend fun getTopicPage(topicId: Int) =
        apolloClient.query(TopicsQueryAdapter.getTopicPage(topicId))
            .execute()
            .dataAssertNoErrors
            .topicPage
            .topicPage

    @WorkerThread
    suspend fun updateFavorite(topicId: Int) =
        apolloClient.mutation(TopicsQueryAdapter.updateTopicFavorite(topicId))
            .execute()
            .dataAssertNoErrors
            .updateTopicFavorite
            .newState

    companion object {
        const val TOPICS_PAGE_SIZE = 25
    }
}
