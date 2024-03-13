package com.kropotov.lovehate.ui.viewmodels.contribute

import com.kropotov.lovehate.ui.base.BaseRouter
import com.kropotov.lovehate.ui.fragments.contribute.ContributeFragment
import com.kropotov.lovehate.ui.fragments.topicPage.TopicPageFragment
import javax.inject.Inject

class ContributeRouter @Inject constructor(
    val fragment: ContributeFragment
): BaseRouter(fragment.childFragmentManager)