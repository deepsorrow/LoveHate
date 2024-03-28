package com.kropotov.lovehate.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.kropotov.lovehate.R
import com.kropotov.lovehate.ui.screens.topicpage.fragments.TopicPageFragment

abstract class BaseRouter(
    private val fragmentManager: FragmentManager
) {

    fun goBack() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        }
    }

    fun navigateToNewTopic(topicId: Int)
        = navigateWithSlideUpTransition(TopicPageFragment.newInstance(topicId))

    protected fun navigateWithSlideUpTransition(
        fragment: Fragment,
        container: Int = R.id.overlay_container
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
