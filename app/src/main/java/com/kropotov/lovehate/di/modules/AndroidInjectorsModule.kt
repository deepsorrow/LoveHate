@file:Suppress("unused")

package com.kropotov.lovehate.di.modules

import com.kropotov.lovehate.di.modules.fragments.OpinionsFragmentModule
import com.kropotov.lovehate.di.modules.fragments.TopicPageFragmentModule
import com.kropotov.lovehate.di.modules.fragments.TopicsFragmentModule
import com.kropotov.lovehate.MainScreenActivity
import com.kropotov.lovehate.ui.fragments.contribute.ContributeFragment
import com.kropotov.lovehate.ui.fragments.feed.FeedFragment
import com.kropotov.lovehate.ui.fragments.OpinionsFragment
import com.kropotov.lovehate.ui.fragments.topicPage.TopicPageFragment
import com.kropotov.lovehate.ui.fragments.topics.TopicsFragment
import com.kropotov.lovehate.ui.fragments.topics.TopicsHostFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AndroidInjectorsModule {

    @ContributesAndroidInjector
    fun injectMainActivity(): MainScreenActivity

    @ContributesAndroidInjector
    fun injectFeedFragment(): FeedFragment

    @ContributesAndroidInjector(modules = [OpinionsFragmentModule::class])
    fun injectOpinionsFragment(): OpinionsFragment

    @ContributesAndroidInjector
    fun injectTopicsHostScreenFragment(): TopicsHostFragment

    @ContributesAndroidInjector(modules = [TopicsFragmentModule::class])
    fun injectTopicsCategoryFragment(): TopicsFragment

    @ContributesAndroidInjector
    fun injectContributeFragment(): ContributeFragment

    @ContributesAndroidInjector(modules = [TopicPageFragmentModule::class])
    fun injectTopicPageFragment(): TopicPageFragment
}