package com.kropotov.lovehate.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.api.BackendQueryAdapter
import com.kropotov.lovehate.fragment.TopicListItem
import com.kropotov.lovehate.type.OpinionType
import com.kropotov.lovehate.ui.viewmodels.topics.TopicListItemVm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopicsRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {
    fun getTopicsStream(topicsSortType: TopicsSortType): Flow<PagingData<TopicListItem>> = Pager(
        config = PagingConfig(enablePlaceholders = true, pageSize = TOPICS_PAGE_SIZE),
        pagingSourceFactory = { TopicsPagingSource(apolloClient, topicsSortType) }
    ).flow

    suspend fun createTopic(
        title: String,
        userId: Int,
        opinionType: OpinionSortType,
        comment: String
    ) = withContext(Dispatchers.IO) {
        apolloClient.mutation(BackendQueryAdapter.createTopic(title, userId, opinionType, comment))
            .execute().data?.addTopic?.topic
    }

    suspend fun getSimilarTopics(topicId: Int) = withContext(Dispatchers.IO) {
        apolloClient.query(BackendQueryAdapter.getSimilarTopics(topicId))
            .execute().data?.similarTopics?.map { topic -> TopicListItemVm(topic.topicListItem) }
    }

    suspend fun getTopicPage(topicId: Int) = withContext(Dispatchers.IO) {
        apolloClient.query(BackendQueryAdapter.getTopicPage(topicId))
            .execute().data?.topicPage?.topicPage
    }

    private companion object {
        const val TOPICS_PAGE_SIZE = 25
    }
}