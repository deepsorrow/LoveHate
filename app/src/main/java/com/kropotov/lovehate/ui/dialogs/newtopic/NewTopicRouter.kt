package com.kropotov.lovehate.ui.dialogs.newtopic

import androidx.core.os.bundleOf
import com.kropotov.lovehate.ui.base.BaseRouter
import javax.inject.Inject

class NewTopicRouter @Inject constructor(
    val fragment: NewTopicDialog
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
