package com.kropotov.lovehate.ui.screens.ratings

import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.data.items.OpinionRatingListItem
import com.kropotov.lovehate.data.items.UserRatingListItem
import com.kropotov.lovehate.data.repositories.OpinionsRepository
import com.kropotov.lovehate.data.repositories.UsersRepository
import com.kropotov.lovehate.type.OpinionsListType
import com.kropotov.lovehate.type.OpinionsListType.MOST_LIKED
import com.kropotov.lovehate.type.OpinionsListType.MOST_DISLIKED
import com.kropotov.lovehate.data.UsersListType
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

class RatingsViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    val toolbar: RatingsToolbar,
    private val usersRepository: UsersRepository,
    private val opinionsRepository: OpinionsRepository
): BaseViewModel(resourceProvider) {

    private val _navigateToUsersList = MutableSharedFlow<UsersListType>()
    val navigateToUsersList: SharedFlow<UsersListType> = _navigateToUsersList

    private val _navigateToOpinionsList = MutableSharedFlow<OpinionsListType>()
    val navigateToOpinionsList: SharedFlow<OpinionsListType> = _navigateToOpinionsList

    val usersDataStream = flow {
        userRatingTypes.forEach { type ->
            val userListItem = usersRepository.getFirstUser(type)
            emit(UserRatingListItem(type, userListItem.username, getUserRatingAction(type)))
        }
    }
        .buffer()
        .shareInThisScope()
    val opinionsDataStream = flow {
        opinionRatingTypes.forEach { type ->
            val opinionListItem = opinionsRepository.getFirstOpinion(type)
            emit(OpinionRatingListItem(type, opinionListItem.text, getOpinionRatingAction(type)))
        }
    }
        .buffer()
        .shareInThisScope()


    fun getUsersEmptyData(): List<UserRatingListItem> =
        mutableListOf<UserRatingListItem>().apply {
            userRatingTypes.forEach { type ->
                add(UserRatingListItem(type, action = getUserRatingAction(type)))
            }
        }

    fun getOpinionsEmptyData(): List<OpinionRatingListItem> =
        mutableListOf<OpinionRatingListItem>().apply {
            opinionRatingTypes.forEach { type ->
                add(OpinionRatingListItem(type, action = getOpinionRatingAction(type)))
            }
        }

    private fun getUserRatingAction(type: UsersListType): () -> Unit = {
        viewModelScope.launch {
            _navigateToUsersList.emit(type)
        }
        Unit
    }

    private fun getOpinionRatingAction(type: OpinionsListType): () -> Unit = {
        viewModelScope.launch {
            _navigateToOpinionsList.emit(type)
        }
        Unit
    }

    private fun <T> Flow<T>.shareInThisScope() =
        shareIn(
            viewModelScope.plus( Dispatchers.IO + defaultExceptionHandler),
            SharingStarted.Eagerly,
            replay
        )

    private companion object {
        val userRatingTypes = UsersListType.entries.toList()
        val opinionRatingTypes = listOf(MOST_LIKED, MOST_DISLIKED)
        const val replay = 5
    }
}
