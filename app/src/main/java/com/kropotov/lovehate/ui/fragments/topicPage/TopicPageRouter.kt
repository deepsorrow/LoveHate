package com.kropotov.lovehate.ui.fragments.topicPage

import com.kropotov.lovehate.ui.base.BaseRouter
import javax.inject.Inject

class TopicPageRouter @Inject constructor(
    val fragment: TopicPageFragment
) : BaseRouter(fragment.childFragmentManager)