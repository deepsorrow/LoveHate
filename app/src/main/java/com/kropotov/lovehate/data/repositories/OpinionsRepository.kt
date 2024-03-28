package com.kropotov.lovehate.data.repositories

import androidx.annotation.WorkerThread
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.api.main.OpinionsQueryAdapter
import com.kropotov.lovehate.data.pagingsources.OpinionsPagingSource
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.di.MainActivityScope
import com.kropotov.lovehate.fragment.OpinionListItem
import com.kropotov.lovehate.type.OpinionsListType
import com.kropotov.lovehate.type.ReactionType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@MainActivityScope
class OpinionsRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {
    fun getOpinionsStream(
        topicId: Int?,
        opinionType: OpinionType,
        listType: OpinionsListType
    ): Flow<PagingData<OpinionListItem>> = Pager(
        config = PagingConfig(enablePlaceholders = false, pageSize = OPINIONS_PAGE_SIZE),
        pagingSourceFactory = {
            OpinionsPagingSource(
                apolloClient,
                topicId,
                opinionType,
                listType
            )
        }
    ).flow

    @WorkerThread
    suspend fun updateFavorite(opinionId: Int) =
        apolloClient.mutation(OpinionsQueryAdapter.updateOpinionFavorite(opinionId))
            .execute()
            .dataAssertNoErrors
            .updateOpinionFavorite
            .newState

    @WorkerThread
    suspend fun updateReaction(opinionId: Int, type: ReactionType) =
        apolloClient.mutation(OpinionsQueryAdapter.updateOpinionReaction(opinionId, type))
            .execute()
            .dataAssertNoErrors
            .updateOpinionReaction
            .newState

    @WorkerThread
    suspend fun getFirstOpinion(listType: OpinionsListType) =
        apolloClient
            .query(OpinionsQueryAdapter.getOpinions(true, listType, 0))
            .execute()
            .dataAssertNoErrors
            .opinions
            .results
            .first()
            .opinionListItem

    private companion object {
        const val OPINIONS_PAGE_SIZE = 10
    }
}
