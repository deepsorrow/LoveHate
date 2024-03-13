package com.kropotov.lovehate.api

import com.apollographql.apollo3.api.Optional
import com.kropotov.lovehate.CreateTopicMutation
import com.kropotov.lovehate.EditTopicMutation
import com.kropotov.lovehate.GetSimilarTopicsQuery
import com.kropotov.lovehate.GetTopicPageQuery
import com.kropotov.lovehate.GetTopicQuery
import com.kropotov.lovehate.GetTopicsQuery
import com.kropotov.lovehate.LatestOpinionsQuery
import com.kropotov.lovehate.PublishOpinionMutation
import com.kropotov.lovehate.data.OpinionSortType
import com.kropotov.lovehate.type.TopicsSortType
import com.kropotov.lovehate.type.OpinionType as GeneratedOpinionType

object BackendQueryAdapter {

    fun getTopic(id: Int) = GetTopicQuery(id)

    fun getTopicPage(id: Int) = GetTopicPageQuery(id)

    fun getTopics(sortType: TopicsSortType, page: Int) = GetTopicsQuery(
        topicsSortType = Optional.presentIfNotNull(sortType),
        page = page
    )

    fun getSimilarTopics(topicId: Int) = GetSimilarTopicsQuery(topicId)

    fun createTopic(title: String, userId: Int, opinionSortType: OpinionSortType, comment: String)
            = CreateTopicMutation(
        title = title,
        userId = userId,
        opinionType = opinionSortType.mapToGenerated(),
        comment = comment
    )

    fun editTopic(id: Int, text: String) = EditTopicMutation(id, text)

    fun latestOpinions(topicId: Int?, opinionSortType: OpinionSortType?, page: Int) = LatestOpinionsQuery(
        topicId = Optional.presentIfNotNull(topicId),
        opinionType = Optional.presentIfNotNull(opinionSortType?.mapToGenerated()),
        page = page
    )

    fun publishOpinion(topicId: Int, userId: Int, text: String, type: OpinionSortType)
            = PublishOpinionMutation(
        topicId = topicId,
        userId = userId,
        text = text,
        type = type.mapToGenerated()
    )

    private fun OpinionSortType.mapToGenerated() = GeneratedOpinionType.valueOf(this.name)
}
