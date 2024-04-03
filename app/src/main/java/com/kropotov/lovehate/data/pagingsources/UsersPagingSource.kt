package com.kropotov.lovehate.data.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.api.main.UsersQueryAdapter
import com.kropotov.lovehate.fragment.UserListItem
import com.kropotov.lovehate.data.UsersListType

class UsersPagingSource(
    private val apolloClient: ApolloClient,
    private val listType: UsersListType
) : PagingSource<Int, UserListItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserListItem> {
        val page = params.key ?: 0
        return try {
            val response = apolloClient
                .query(UsersQueryAdapter.getUsers(false, listType, page))
                .execute()
                .dataAssertNoErrors
                .users

            LoadResult.Page(
                data = response.results.map { it.userListItem },
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (page == response.totalPages) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UserListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}
