package com.kropotov.lovehate.api.main

import com.apollographql.apollo3.api.Optional
import com.kropotov.lovehate.CreateTopicMutation
import com.kropotov.lovehate.GetSimilarTopicsQuery
import com.kropotov.lovehate.GetTopicPageQuery
import com.kropotov.lovehate.GetTopicsQuery
import com.kropotov.lovehate.UpdateTopicFavoriteMutation
import com.kropotov.lovehate.api.main.OpinionsQueryAdapter.mapToGenerated
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.type.TopicsListType

object TopicsQueryAdapter {

    fun getTopicPage(id: Int) = GetTopicPageQuery(id)

    fun getTopics(sortType: TopicsListType, searchQuery: String, page: Int) = GetTopicsQuery(
        listType = Optional.presentIfNotNull(sortType),
        searchQuery = Optional.presentIfNotNull(searchQuery),
        page = page
    )

    fun getSimilarTopics(topicId: Int) = GetSimilarTopicsQuery(topicId)

    fun createTopic(title: String, opinionType: OpinionType, opinionText: String)
            = CreateTopicMutation(
        title = title,
        opinionType = opinionType.mapToGenerated(),
        opinionText = opinionText
    )

    fun updateTopicFavorite(topicId: Int) = UpdateTopicFavoriteMutation(topicId)
}
