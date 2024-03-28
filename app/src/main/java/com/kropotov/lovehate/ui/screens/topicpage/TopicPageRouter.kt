package com.kropotov.lovehate.ui.screens.topicpage

import com.kropotov.lovehate.ui.base.BaseRouter
import com.kropotov.lovehate.ui.dialogs.NewOpinionDialog
import com.kropotov.lovehate.ui.screens.feed.fragments.FeedFragment
import com.kropotov.lovehate.ui.screens.topicpage.fragments.TopicPageFragment
import com.kropotov.lovehate.ui.screens.topicpage.fragments.TopicPageFragment.Companion.TOPIC_PAGE_ID
import javax.inject.Inject
import javax.inject.Named

class TopicPageRouter @Inject constructor(
    @Named(TOPIC_PAGE_ID) private val topicId: Int,
    val fragment: TopicPageFragment
) : BaseRouter(fragment.childFragmentManager) {

    fun popBackStack() {
        fragment.parentFragmentManager.popBackStack()
    }

    fun showOpinions() =
        navigateWithSlideUpTransition(FeedFragment.newInstance(topicId))

    fun showNewPostDialog() {
        NewOpinionDialog
            .newInstance(topicId)
            .show(fragment.childFragmentManager, fragment::class.simpleName)
    }
}