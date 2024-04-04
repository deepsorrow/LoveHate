package com.kropotov.lovehate.ui.screens.topics

import com.kropotov.lovehate.ui.base.BaseRouter
import com.kropotov.lovehate.ui.screens.topicpage.fragments.TopicPageFragment
import com.kropotov.lovehate.ui.screens.topics.fragments.TopicsFragment
import javax.inject.Inject

class TopicsRouter @Inject constructor(
    val fragment: TopicsFragment
): BaseRouter(fragment.parentFragmentManager) {

    fun navigateToTopicPage(topicId: Int, thumbnailUrl: String) =
        navigateWithSlideUpTransition(TopicPageFragment.newInstance(topicId, thumbnailUrl))
}
