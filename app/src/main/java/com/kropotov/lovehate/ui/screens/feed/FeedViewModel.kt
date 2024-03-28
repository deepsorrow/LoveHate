package com.kropotov.lovehate.ui.screens.feed

import com.kropotov.lovehate.R
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.screens.feed.fragments.FeedFragment.Companion.FEED_TOPIC_ID_ARG
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import javax.inject.Inject
import javax.inject.Named

class FeedViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    val toolbar: FeedToolbar,
    @Named(FEED_TOPIC_ID_ARG) val topicId: Int?
) : BaseViewModel(resourceProvider) {

    val hasToolbar = topicId == null
    val topPadding = if (hasToolbar) 0 else R.dimen.toolbar_status_bar_padding

    init {
        if (!hasToolbar) {
            toolbar.toolbarVisibility.set(false)
        }
    }
}
