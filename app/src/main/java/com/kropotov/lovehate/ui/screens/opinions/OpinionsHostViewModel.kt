package com.kropotov.lovehate.ui.screens.opinions

import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.screens.opinions.fragments.OpinionsHostFragment.Companion.FEED_TOPIC_ID_ARG
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class OpinionsHostViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    val toolbar: OpinionsHostToolbar,
    @Named(FEED_TOPIC_ID_ARG) val topicId: Int?
) : BaseViewModel(resourceProvider) {

    init {
        if (topicId != null) {
            toolbar.title.set(R.string.opinions)
            toolbar.arrowBackIsVisible.set(true)
            toolbar.isSubtitleVisible.set(false)
            toolbar.searchIconIsVisible.set(true)
        }

        toolbar.subscribeToSearchTextQuery { text ->
            viewModelScope.launch {
                _searchQuery.emit(text)
            }
        }
    }
}
