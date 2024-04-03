package com.kropotov.lovehate.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.fragment.app.commit
import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.ActivityAuthBinding
import com.kropotov.lovehate.ui.screens.auth.fragments.LoginFragment
import com.kropotov.lovehate.ui.utilities.SharedPreferencesHelper
import com.kropotov.lovehate.ui.utilities.adjustSystemBarIconsColor
import com.kropotov.lovehate.ui.utilities.autoCleared
import com.kropotov.lovehate.ui.screens.auth.AuthRouter
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class AuthActivity : DaggerAppCompatActivity() {

    private var binding by autoCleared<ActivityAuthBinding>()

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    @Inject
    lateinit var router: AuthRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        if (sharedPreferencesHelper.isUserAuthenticated()) {
            val intent = Intent(this, MainScreenActivity::class.java)
            startActivity(intent)
            finish()
        } else if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.container, LoginFragment.newInstance())
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.adjustSystemBarIconsColor()
    }

}
