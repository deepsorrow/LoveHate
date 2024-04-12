package com.kropotov.lovehate.ui.screens.profile.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.onNotificationsSwitched(true)
        } else {
            binding.notificationsSwitch.isChecked = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.router = router
        // Update current theme on activity recreate
        binding.currentTheme.text = getString(viewModel.sharedPrefs.getPreferredTheme().titleResId)
        binding.notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            checkPermissionAndSaveNotificationsSetting(isChecked)
        }
    }

    @SuppressLint("InlinedApi")
    private fun checkPermissionAndSaveNotificationsSetting(isChecked: Boolean) {
        if (isChecked && SDK_INT >= Build.VERSION_CODES.TIRAMISU
            && shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            viewModel.onNotificationsSwitched(isChecked)
        }
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
