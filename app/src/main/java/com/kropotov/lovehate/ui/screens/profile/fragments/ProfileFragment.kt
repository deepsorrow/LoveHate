package com.kropotov.lovehate.ui.screens.profile.fragments

import android.os.Bundle
import android.view.View
import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.FragmentProfileBinding
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.screens.profile.ProfileRouter
import com.kropotov.lovehate.ui.screens.profile.ProfileViewModel
import javax.inject.Inject

class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>(
    R.layout.fragment_profile
) {

    override val vmClass = ProfileViewModel::class.java

    @Inject
    lateinit var router: ProfileRouter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.router = router
        // Update current theme on activity recreate
        binding.currentTheme.text = getString(viewModel.sharedPrefs.getPreferredTheme().titleResId)
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
