package com.kropotov.lovehate.data.repositories

import androidx.annotation.WorkerThread
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.api.main.UsersQueryAdapter
import com.kropotov.lovehate.data.pagingsources.UsersPagingSource
import com.kropotov.lovehate.di.MainActivityScope
import com.kropotov.lovehate.fragment.UserListItem
import com.kropotov.lovehate.data.UsersListType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@MainActivityScope
class UsersRepository @Inject constructor (
    val apolloClient: ApolloClient
) {

    fun getUsersStream(
        listType: UsersListType
    ): Flow<PagingData<UserListItem>> = Pager(
        config = PagingConfig(enablePlaceholders = false, pageSize = USERS_PAGE_SIZE),
        pagingSourceFactory = { UsersPagingSource(apolloClient, listType) }
    ).flow

    @WorkerThread
    suspend fun getFirstUser(
        listType: UsersListType
    ) = apolloClient
        .query(UsersQueryAdapter.getUsers(true, listType, 0))
        .execute()
        .dataAssertNoErrors
        .users
        .results
        .first()
        .userListItem

    @WorkerThread
    suspend fun getCurrentUser() = apolloClient
        .query(UsersQueryAdapter.getCurrentUser())
        .execute()
        .dataAssertNoErrors
        .user
        ?.userListItem

    private companion object {
        const val USERS_PAGE_SIZE = 10
    }
}
