package com.kropotov.lovehate.ui

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.color.MaterialColors
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.databinding.ActivityMainScreenBinding
import com.kropotov.lovehate.ui.adapters.MainScreenViewPagerAdapter
import com.kropotov.lovehate.ui.utils.autoCleared
import com.kropotov.lovehate.ui.utils.getColorAttr
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainScreenActivity : AppCompatActivity(), HasAndroidInjector {

    private var binding by autoCleared<ActivityMainScreenBinding>()
    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Any>
    //@Inject lateinit var sharedPrefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        AndroidInjection.inject(this)
        //setTheme(sharedPrefs.getPreferredTheme())
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater).apply {
            setContentView(root)

            pagerContainer.adapter = MainScreenViewPagerAdapter(this@MainScreenActivity)
            pagerContainer.isUserInputEnabled = false
            pagerContainer.offscreenPageLimit = 2

            initBottomBar()
        }

        registerFragmentResultListener()
    }

    private fun registerFragmentResultListener() {
        supportFragmentManager.setFragmentResultListener(
            CHANGE_CONTAINER_COLOR, this
        ) { _, result ->
            val feelingTypeInd = result.getInt(NEW_FEELING_TYPE, 0)
            val opinionType = OpinionType.entries[feelingTypeInd]
            val toolbarColor = this@MainScreenActivity.getColorAttr(opinionType.color, Color.BLUE)
            val containerColor = this@MainScreenActivity.getColorAttr(opinionType.containerColor)

            binding.pagerContainer.setBackgroundColor(containerColor)
            window.statusBarColor = toolbarColor
        }
    }

    private fun ActivityMainScreenBinding.initBottomBar() {
        bottomBar.setOnItemSelectedListener { item ->
            val nextScreenId = when (item.itemId) {
                R.id.menu_item_feed -> 0
                R.id.menu_item_topics -> 1
                R.id.menu_item_create_new_topic -> 2
                R.id.menu_item_ratings -> 3
                R.id.menu_item_profile -> 4
                else -> throw IllegalArgumentException("Неверный номер экрана!")
            }
            nextScreenId > 2 && return@setOnItemSelectedListener false
            setDefaultColorOnTop()
            binding.pagerContainer.currentItem = nextScreenId
            true
        }
    }

    private fun setDefaultColorOnTop() {
        val color = this@MainScreenActivity.getColorAttr(androidx.appcompat.R.attr.colorPrimary, Color.BLUE)
        window.statusBarColor = color
    }

    override fun androidInjector() = androidInjector

    companion object {
        const val CHANGE_CONTAINER_COLOR = "change_container_color_event"
        const val NEW_FEELING_TYPE = "new_feeling_type"
    }
}