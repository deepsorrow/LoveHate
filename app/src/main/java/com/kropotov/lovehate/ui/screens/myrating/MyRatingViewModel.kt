package com.kropotov.lovehate.ui.screens.myrating

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kropotov.lovehate.data.repositories.MyRatingRepository
import com.kropotov.lovehate.fragment.MyRatedEvent
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MyRatingViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    val toolbar: MyRatingToolbar,
    val repository: MyRatingRepository
): BaseViewModel(resourceProvider) {

    fun searchMyRatingItems(): Flow<PagingData<MyRatedEvent>> =
        repository.getMyRatingItems().cachedIn(viewModelScope)
}