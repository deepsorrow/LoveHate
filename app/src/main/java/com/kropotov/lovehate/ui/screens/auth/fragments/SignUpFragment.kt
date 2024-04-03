package com.kropotov.lovehate.ui.screens.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.FragmentAuthSignupBinding
import com.kropotov.lovehate.ui.AuthActivity
import com.kropotov.lovehate.ui.MainScreenActivity
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.textChanges
import com.kropotov.lovehate.ui.screens.auth.SignUpViewModel
import com.kropotov.lovehate.ui.screens.auth.SignUpViewModel.Companion.CHECK_USERNAME_DEBOUNCE
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SignUpFragment : BaseFragment<SignUpViewModel, FragmentAuthSignupBinding>(
    R.layout.fragment_auth_signup
) {

    override val vmClass = SignUpViewModel::class.java

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            router = (requireActivity() as AuthActivity).router

            login.textChanges()
                .debounce(CHECK_USERNAME_DEBOUNCE)
                .onEach {
                    viewModel?.validateIsNameTaken()
                }
                .launchIn(lifecycleScope)
        }

        lifecycleScope.launch {
            viewModel.navigateToMainScreen.collect {
                startActivity(Intent(requireContext(), MainScreenActivity::class.java))
                requireActivity().finish()
            }
        }
    }

    companion object {

        fun newInstance() = SignUpFragment()
    }
}
