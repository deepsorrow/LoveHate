package com.kropotov.lovehate.ui.screens.topics

import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopicsHostViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    val toolbar: TopicsHostToolbar
) : BaseViewModel(resourceProvider) {

    init {
        toolbar.subscribeToSearchTextQuery { text ->
            viewModelScope.launch {
                _searchQuery.emit(text)
            }
        }
    }
}
