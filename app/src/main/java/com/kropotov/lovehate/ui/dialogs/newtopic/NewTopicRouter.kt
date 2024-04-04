package com.kropotov.lovehate.ui.dialogs.newtopic

import com.kropotov.lovehate.R
import com.kropotov.lovehate.ui.base.BaseRouter
import com.kropotov.lovehate.ui.screens.topicpage.fragments.TopicPageFragment
import javax.inject.Inject

class NewTopicRouter @Inject constructor(
    val fragment: NewTopicDialog
) : BaseRouter(fragment.childFragmentManager) {

    fun navigateToTopicPage(topicId: Int) = navigateWithSlideUpTransition(
        TopicPageFragment.newInstance(topicId),
        fragment.parentFragmentManager,
        R.id.main_overlay_container
    )
}
