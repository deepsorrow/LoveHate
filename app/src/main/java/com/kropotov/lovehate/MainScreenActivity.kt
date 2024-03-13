package com.kropotov.lovehate

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kropotov.lovehate.data.OpinionSortType
import com.kropotov.lovehate.databinding.ActivityMainScreenBinding
import com.kropotov.lovehate.ui.adapters.CONTRIBUTE_PAGE_INDEX
import com.kropotov.lovehate.ui.adapters.FEED_PAGE_INDEX
import com.kropotov.lovehate.ui.adapters.MainScreenViewPagerAdapter
import com.kropotov.lovehate.ui.adapters.PROFILE_PAGE_INDEX
import com.kropotov.lovehate.ui.adapters.RATINGS_PAGE_INDEX
import com.kropotov.lovehate.ui.adapters.TOPICS_HOST_PAGE_INDEX
import com.kropotov.lovehate.ui.utilities.autoCleared
import com.kropotov.lovehate.ui.utilities.getColorAttr
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
            val opinionSortType = OpinionSortType.entries[feelingTypeInd]
            val toolbarColor = this@MainScreenActivity.getColorAttr(opinionSortType.color, Color.BLUE)
            val containerColor = this@MainScreenActivity.getColorAttr(opinionSortType.containerColor)

            binding.pagerContainer.setBackgroundColor(containerColor)
            window.statusBarColor = toolbarColor
        }
    }

    private fun ActivityMainScreenBinding.initBottomBar() {
        bottomBar.setOnItemSelectedListener { item ->
            val nextScreenId = when (item.itemId) {
                R.id.menu_item_feed             -> FEED_PAGE_INDEX
                R.id.menu_item_topics           -> TOPICS_HOST_PAGE_INDEX
                R.id.menu_item_create_new_topic -> CONTRIBUTE_PAGE_INDEX
                R.id.menu_item_ratings          -> RATINGS_PAGE_INDEX
                R.id.menu_item_profile          -> PROFILE_PAGE_INDEX
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