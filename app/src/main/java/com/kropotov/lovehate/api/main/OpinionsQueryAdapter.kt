package com.kropotov.lovehate.api.main

import com.apollographql.apollo3.api.Optional
import com.kropotov.lovehate.GetOpinionsQuery
import com.kropotov.lovehate.PublishOpinionMutation
import com.kropotov.lovehate.UpdateOpinionFavoriteMutation
import com.kropotov.lovehate.UpdateOpinionReactionMutation
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.type.OpinionType as GeneratedOpinionType
import com.kropotov.lovehate.type.OpinionsListType
import com.kropotov.lovehate.type.ReactionType

object OpinionsQueryAdapter {

    fun getLatestOpinions(topicId: Int?, opinionType: OpinionType?, page: Int): GetOpinionsQuery {
        val sortType = if (opinionType == OpinionType.UNION)  {
            null
        } else {
            opinionType
        }

        return GetOpinionsQuery(
            onlyFirst = false,
            topicId = Optional.presentIfNotNull(topicId),
            opinionType = Optional.presentIfNotNull(sortType?.mapToGenerated()),
            page = page
        )
    }

    fun getOpinions(onlyFirst: Boolean, listType: OpinionsListType, page: Int): GetOpinionsQuery =
        GetOpinionsQuery(
            onlyFirst = onlyFirst,
            listType = Optional.presentIfNotNull(listType),
            page = page
        )

    fun publishOpinion(topicId: Int, text: String, type: OpinionType)
            = PublishOpinionMutation(
        topicId = topicId,
        text = text,
        type = type.mapToGenerated()
    )

    fun updateOpinionFavorite(opinionId: Int) = UpdateOpinionFavoriteMutation(opinionId)

    fun updateOpinionReaction(opinionId: Int, type: ReactionType) =
        UpdateOpinionReactionMutation(opinionId, type)

    fun OpinionType.mapToGenerated() = GeneratedOpinionType.valueOf(this.name)
}
