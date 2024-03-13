package com.kropotov.lovehate.ui.viewmodels.topics

import com.kropotov.lovehate.ui.base.BaseRouter
import com.kropotov.lovehate.ui.fragments.topics.TopicsFragment
import javax.inject.Inject

class TopicsRouter @Inject constructor(
    val fragment: TopicsFragment
): BaseRouter(fragment.parentFragmentManager)