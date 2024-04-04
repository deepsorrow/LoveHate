package com.kropotov.lovehate.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.kropotov.lovehate.R

abstract class BaseRouter(
    private val fragmentManager: FragmentManager
) {

    fun goBack() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        }
    }

    protected fun navigateWithSlideUpTransition(
        fragment: Fragment,
        usedFragmentManager: FragmentManager = fragmentManager,
        container: Int = R.id.overlay_container
    ) {
        usedFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_up,
                R.anim.slide_down,
                R.anim.slide_up,
                R.anim.slide_down
            )
            add(container, fragment)
            addToBackStack(null)
        }
    }

    protected fun navigateWithSlideRightTransition(
        fragment: Fragment,
        container: Int = R.id.overlay_container
    ) {
        fragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in_from_right,
                R.anim.slide_out_to_right,
                R.anim.slide_in_from_right,
                R.anim.slide_out_to_right
            )
            add(container, fragment)
            addToBackStack(null)
        }
    }
}
