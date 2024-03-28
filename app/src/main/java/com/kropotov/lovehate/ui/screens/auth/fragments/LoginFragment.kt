package com.kropotov.lovehate.ui.screens.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.FragmentAuthLoginBinding
import com.kropotov.lovehate.ui.AuthActivity
import com.kropotov.lovehate.ui.MainScreenActivity
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.screens.auth.SharedAuthViewModel
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<SharedAuthViewModel, FragmentAuthLoginBinding>(
    R.layout.fragment_auth_login
) {

    override val isActivityViewModelOwner = true
    override val vmClass = SharedAuthViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.router = (requireActivity() as AuthActivity).router

        lifecycleScope.launch {
            viewModel.navigateToMainScreen.collect {
                startActivity(Intent(requireContext(), MainScreenActivity::class.java))
                requireActivity().finish()
            }
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}
