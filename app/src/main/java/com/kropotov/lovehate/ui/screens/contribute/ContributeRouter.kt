package com.kropotov.lovehate.ui.screens.contribute

import androidx.core.os.bundleOf
import com.kropotov.lovehate.ui.base.BaseRouter
import com.kropotov.lovehate.ui.screens.contribute.fragments.ContributeFragment
import javax.inject.Inject

class ContributeRouter @Inject constructor(
    val fragment: ContributeFragment
): BaseRouter(fragment.childFragmentManager) {

    fun informAboutNewContent() {
        val bundle = bundleOf(NEW_TOPIC_EVENT_KEY to Unit)
        fragment.parentFragmentManager.setFragmentResult(NEW_TOPIC_EVENT_KEY, bundle)
    }

    fun pickMedia() {

    }

    companion object {
        const val NEW_TOPIC_EVENT_KEY = "new_topic_event_key"
    }
}
