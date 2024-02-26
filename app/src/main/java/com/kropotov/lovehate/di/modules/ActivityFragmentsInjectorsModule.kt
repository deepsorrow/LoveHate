@file:Suppress("unused")

package com.kropotov.lovehate.di.modules

import com.kropotov.lovehate.ui.MainScreenActivity
import com.kropotov.lovehate.ui.fragments.FeedFragment
import com.kropotov.lovehate.ui.fragments.FeelingScreenFragment
import com.kropotov.lovehate.ui.fragments.TopicsCategoryScreenFragment
import com.kropotov.lovehate.ui.fragments.TopicsHostScreenFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityFragmentInjectorsModule {

    @ContributesAndroidInjector
    fun injectMainActivity(): MainScreenActivity

    @ContributesAndroidInjector
    fun injectFeedFragment(): FeedFragment

    @ContributesAndroidInjector
    fun injectFeelingScreenFragment(): FeelingScreenFragment

    @ContributesAndroidInjector
    fun injectTopicsHostScreenFragment(): TopicsHostScreenFragment

    @ContributesAndroidInjector
    fun injectTopicsCategoryScreenFragment(): TopicsCategoryScreenFragment
}