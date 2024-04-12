package com.kropotov.lovehate.api.main

import com.apollographql.apollo3.api.Optional
import com.kropotov.lovehate.GetNotificationsQuery
import com.kropotov.lovehate.GetOpinionsQuery
import com.kropotov.lovehate.PublishOpinionMutation
import com.kropotov.lovehate.UpdateOpinionFavoriteMutation
import com.kropotov.lovehate.UpdateOpinionReactionMutation
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.type.OpinionType as GeneratedOpinionType
import com.kropotov.lovehate.type.OpinionsListType
import com.kropotov.lovehate.type.ReactionType

object OpinionsQueryAdapter {

    fun getLatestOpinions(
        topicId: Int?,
        opinionType: OpinionType?,
        searchQuery: String,
        page: Int
    ): GetOpinionsQuery {
        val sortType = if (opinionType == OpinionType.UNION) {
            null
        } else {
            opinionType
        }

        return GetOpinionsQuery(
            onlyFirst = false,
            topicId = Optional.presentIfNotNull(topicId),
            opinionType = Optional.presentIfNotNull(sortType?.mapToGenerated()),
            searchQuery = Optional.presentIfNotNull(searchQuery),
            page = page
        )
    }

    fun getOpinions(
        onlyFirst: Boolean,
        listType: OpinionsListType,
        searchQuery: String,
        page: Int
    ): GetOpinionsQuery = GetOpinionsQuery(
        onlyFirst = onlyFirst,
        listType = Optional.presentIfNotNull(listType),
        searchQuery = Optional.presentIfNotNull(searchQuery),
        page = page
    )

    fun publishOpinion(topicId: Int, text: String, type: OpinionType) = PublishOpinionMutation(
        topicId = topicId,
        text = text,
        type = type.mapToGenerated()
    )

    fun updateOpinionFavorite(opinionId: Int) = UpdateOpinionFavoriteMutation(opinionId)

    fun updateOpinionReaction(opinionId: Int, type: ReactionType) =
        UpdateOpinionReactionMutation(opinionId, type)

    fun getNotifications() = GetNotificationsQuery()

    fun OpinionType.mapToGenerated() = GeneratedOpinionType.valueOf(this.name)
}
