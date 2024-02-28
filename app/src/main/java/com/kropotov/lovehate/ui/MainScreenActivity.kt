package com.kropotov.lovehate.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.android.material.color.MaterialColors
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.databinding.ActivityMainScreenBinding
import com.kropotov.lovehate.ui.adapters.MainScreenViewPagerAdapter
import com.kropotov.lovehate.ui.utils.autoCleared
import com.kropotov.lovehate.ui.vm.ToolbarVm
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainScreenActivity : AppCompatActivity(), HasAndroidInjector {

    private var binding by autoCleared<ActivityMainScreenBinding>()
    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject lateinit var toolbarVm: ToolbarVm
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

        subscribeToToolbarEvents()
        registerFragmentResultListener()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.isNotEmpty()) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun subscribeToToolbarEvents() {
        lifecycleScope.launch {
            toolbarVm.title.collect {
                binding.toolbarLayout.title.text = it
            }
        }

        lifecycleScope.launch {
            toolbarVm.subtitle.collect {
                binding.toolbarLayout.subtitle.text = it
            }
        }

        lifecycleScope.launch {
            toolbarVm.subtitleIsVisible.collect {
                binding.toolbarLayout.subtitle.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            toolbarVm.arrowBackIsVisible.collect {
                binding.toolbarLayout.arrowBack.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            toolbarVm.searchIconIsVisible.collect { it ->
                binding.toolbarLayout.titleIcon.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            toolbarVm.isVisible.collect {
                binding.toolbarLayout.root.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            toolbarVm.isBottomOffsetVisible.collect { isVisible ->
                val offset = this@MainScreenActivity.resources.getDimension(R.dimen.standard_offset).toInt()
                val paddingBottom = if (isVisible) offset else 0
                binding.toolbarLayout.root.run {
                    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
                }
            }
        }
    }

    private fun registerFragmentResultListener() {
        supportFragmentManager.setFragmentResultListener(
            CHANGE_TOOLBAR_COLOR_EVENT, this
        ) { _, result ->
            val feelingTypeInd = result.getInt(NEW_FEELING_TYPE, 0)
            val opinionType = OpinionType.entries[feelingTypeInd]
            val toolbarColor =
                MaterialColors.getColor(this@MainScreenActivity, opinionType.color, Color.BLUE)
            val containerColor = MaterialColors.getColor(
                this@MainScreenActivity,
                opinionType.containerColor,
                Color.WHITE
            )

            binding.toolbarLayout.root.setBackgroundColor(toolbarColor)
            binding.pagerContainer.setBackgroundColor(containerColor)

            window.statusBarColor = toolbarColor
        }
    }

    private fun setDefaultColorOnTop() {
        val color = MaterialColors.getColor(this@MainScreenActivity, androidx.appcompat.R.attr.colorPrimary, Color.BLUE)
        window.statusBarColor = color
        binding.toolbarLayout.root.setBackgroundColor(color)
    }

    private fun ActivityMainScreenBinding.initBottomBar() {
        bottomBar.setOnItemSelectedListener { item ->
            val nextScreenId = when (item.itemId) {
                R.id.menu_item_feed -> {
                    lifecycleScope.launch {
                        toolbarVm.run {
                            title.emit(getString(R.string.app_name))
                            subtitle.emit(getString(R.string.new_messages))
                            subtitleIsVisible.emit(true)
                            arrowBackIsVisible.emit(false)
                            isBottomOffsetVisible.emit(false)
                            searchIconIsVisible.emit(true)
                            isVisible.emit(true)
                        }
                    }

                    0
                }
                R.id.menu_item_topics -> {
                    lifecycleScope.launch {
                        toolbarVm.run {
                            title.emit(getString(R.string.topics))
                            subtitleIsVisible.emit(false)
                            arrowBackIsVisible.emit(false)
                            isBottomOffsetVisible.emit(true)
                            searchIconIsVisible.emit(false)
                            isVisible.emit(true)
                        }
                    }

                    1
                }
                R.id.menu_item_create_new_topic -> {
                    lifecycleScope.launch {
                        toolbarVm.run {
                            title.emit(getString(R.string.new_topic))
                            subtitleIsVisible.emit(false)
                            arrowBackIsVisible.emit(false)
                            isBottomOffsetVisible.emit(true)
                            searchIconIsVisible.emit(false)
                            isVisible.emit(true)
                        }
                    }

                    2
                }
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

    override fun androidInjector() = androidInjector

    companion object {
        const val CHANGE_TOOLBAR_COLOR_EVENT = "change_toolbar_color_event"
        const val NEW_FEELING_TYPE = "new_feeling_type"
    }
}