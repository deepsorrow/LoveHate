package com.kropotov.lovehate.ui.screens.topicpage

import androidx.fragment.app.commit
import com.kropotov.lovehate.R
import com.kropotov.lovehate.ui.base.BaseRouter
import com.kropotov.lovehate.ui.dialogs.newopinion.NewOpinionDialog
import com.kropotov.lovehate.ui.screens.opinions.fragments.OpinionsHostFragment
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
        navigateWithSlideRightTransition(OpinionsHostFragment.newInstance(topicId))

    fun showNewPostDialog() {
        NewOpinionDialog
            .newInstance(topicId)
            .show(fragment.childFragmentManager, fragment::class.simpleName)
    }

    fun navigateToSimilarTopic(topicId: Int, thumbnailUrl: String) =
        navigateWithSlideUpTransition(TopicPageFragment.newInstance(topicId, thumbnailUrl))

}