package com.kropotov.lovehate.data.repositories

import androidx.annotation.WorkerThread
import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.api.main.UsersQueryAdapter
import com.kropotov.lovehate.di.MainActivityScope
import javax.inject.Inject

@MainActivityScope
class FeedbackRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {

    @WorkerThread
    suspend fun sendFeedback(text: String) {
        apolloClient
            .mutation(UsersQueryAdapter.sendFeedback(text))
            .execute()
            .dataAssertNoErrors
            .sendFeedback
    }
}
