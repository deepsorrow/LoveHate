package com.kropotov.lovehate.ui.screens.auth

import androidx.fragment.app.FragmentManager
import com.kropotov.lovehate.ui.base.BaseRouter
import com.kropotov.lovehate.ui.screens.auth.fragments.SignUpFragment

class AuthRouter(fragmentManager: FragmentManager) : BaseRouter(fragmentManager) {

    fun navigateToRegistration() =
        navigateWithSlideUpTransition(SignUpFragment.newInstance())
}
