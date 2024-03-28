package com.kropotov.lovehate.ui.screens.users

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kropotov.lovehate.data.repositories.UsersRepository
import com.kropotov.lovehate.fragment.UserListItem
import com.kropotov.lovehate.data.UsersListType
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsersViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    repository: UsersRepository,
    val toolbar: UsersToolbar,
    val listType: UsersListType
) : BaseViewModel(resourceProvider) {

    var items: Flow<PagingData<UserListItem>> =
        repository.getUsersStream(listType).cachedIn(viewModelScope)
}
