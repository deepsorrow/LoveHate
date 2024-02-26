package com.kropotov.lovehate.ui

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentResultListener
import com.google.android.material.color.MaterialColors
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.FeelingType
import com.kropotov.lovehate.databinding.ActivityMainScreenBinding
import com.kropotov.lovehate.ui.fragments.FeedFragment
import com.kropotov.lovehate.ui.utils.autoCleared
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class MainScreenActivity : AppCompatActivity(), HasAndroidInjector {

    private var binding by autoCleared<ActivityMainScreenBinding>()
    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private val toolbarVm by viewModels()
    //@Inject lateinit var sharedPrefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        //setTheme(sharedPrefs.getPreferredTheme())
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.container, FeedFragment.newInstance(), FeedFragment.tag).commit()
        }

        binding = ActivityMainScreenBinding.inflate(layoutInflater).apply {
            setContentView(root)

            initBottomBar()
        }

        supportFragmentManager.setFragmentResultListener(CHANGE_TOOLBAR_COLOR_EVENT, this, object : FragmentResultListener {
            override fun onFragmentResult(requestKey: String, result: Bundle) {
                val feelingTypeInd = result.getInt(NEW_FEELING_TYPE, 0)
                val feelingType = FeelingType.entries[feelingTypeInd]
                val toolbarColor = MaterialColors.getColor(this@MainScreenActivity, feelingType.color, Color.BLUE)
                val containerColor = MaterialColors.getColor(this@MainScreenActivity, feelingType.containerColor, Color.WHITE)

                binding.toolbarLayout.root.setBackgroundColor(toolbarColor)
                binding.container.setBackgroundColor(containerColor)

                window.statusBarColor = toolbarColor
            }
        })
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

            //supportFragmentManager.beginTransaction().add(
            true
        }
    }

    override fun androidInjector() = androidInjector

    companion object {
        const val CHANGE_TOOLBAR_EVENT = "change_toolbar_event"
        const val NEW_TITLE = "new_title"
        const val NEW_SUBTITLE = "new_subtitle"
        const val NEW_SUBTITLE = "new_subtitle"

        const val CHANGE_TOOLBAR_COLOR_EVENT = "change_toolbar_color_event"
        const val NEW_FEELING_TYPE = "new_feeling_type"
    }
}