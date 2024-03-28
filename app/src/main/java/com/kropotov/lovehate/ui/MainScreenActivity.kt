package com.kropotov.lovehate.ui

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.ActivityMainScreenBinding
import com.kropotov.lovehate.ui.adapters.viewpagers.CONTRIBUTE_PAGE_INDEX
import com.kropotov.lovehate.ui.adapters.viewpagers.FEED_PAGE_INDEX
import com.kropotov.lovehate.ui.adapters.viewpagers.MAIN_SCREEN_PAGE_COUNT
import com.kropotov.lovehate.ui.adapters.viewpagers.MainScreenViewPagerAdapter
import com.kropotov.lovehate.ui.adapters.viewpagers.PROFILE_PAGE_INDEX
import com.kropotov.lovehate.ui.adapters.viewpagers.RATINGS_PAGE_INDEX
import com.kropotov.lovehate.ui.adapters.viewpagers.TOPICS_HOST_PAGE_INDEX
import com.kropotov.lovehate.ui.utilities.SharedPreferencesHelper
import com.kropotov.lovehate.ui.utilities.autoCleared
import com.kropotov.lovehate.ui.utilities.getColorAttr
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainScreenActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    private var binding by autoCleared<ActivityMainScreenBinding>()

    @Inject
    lateinit var sharedPrefs: SharedPreferencesHelper

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        setTheme(sharedPrefs.getPreferredTheme().themeResId)
        super.onCreate(savedInstanceState)

        binding = ActivityMainScreenBinding.inflate(layoutInflater).apply {
            setContentView(root)

            pagerContainer.adapter = MainScreenViewPagerAdapter(this@MainScreenActivity)
            pagerContainer.isUserInputEnabled = false
            pagerContainer.offscreenPageLimit = MAIN_SCREEN_PAGE_COUNT - 1

            initBottomBar()
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun ActivityMainScreenBinding.initBottomBar() {
        bottomBar.setOnItemSelectedListener { item ->
            val nextScreenId = when (item.itemId) {
                R.id.menu_item_feed -> FEED_PAGE_INDEX
                R.id.menu_item_topics -> TOPICS_HOST_PAGE_INDEX
                R.id.menu_item_create_new_topic -> CONTRIBUTE_PAGE_INDEX
                R.id.menu_item_ratings -> RATINGS_PAGE_INDEX
                R.id.menu_item_profile -> PROFILE_PAGE_INDEX
                else -> throw IllegalArgumentException("Invalid screen index!")
            }
            setDefaultContainerColor()
            binding.pagerContainer.currentItem = nextScreenId
            true
        }
    }

    private fun setDefaultContainerColor() {
        val color = getColorAttr(R.attr.background_color, Color.WHITE)
        binding.pagerContainer.setBackgroundColor(color)
    }
}
