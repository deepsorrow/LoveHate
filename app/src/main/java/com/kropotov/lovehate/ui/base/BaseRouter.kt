package com.kropotov.lovehate.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.kropotov.lovehate.R
import com.kropotov.lovehate.ui.fragments.topicPage.TopicPageFragment

open class BaseRouter(
    private val fragmentManager: FragmentManager
) {

    fun navigateToNewTopic(topicId: Int)
        = navigateWithSlideUpTransition(fragment = TopicPageFragment.newInstance(topicId))

    private fun navigateWithSlideUpTransition(
        container: Int = R.id.overlay_container,
        fragment: Fragment
    ) {
        fragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_up,
                R.anim.slide_down,
                0,
                R.anim.slide_down
            )
            add(container, fragment)
            addToBackStack(null)
        }
    }
}