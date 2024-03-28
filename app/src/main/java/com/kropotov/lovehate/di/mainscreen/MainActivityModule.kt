package com.kropotov.lovehate.di.mainscreen

import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.api.main.BackendMainService
import com.kropotov.lovehate.di.MainActivityScope
import com.kropotov.lovehate.di.mainscreen.fragments.FeedFragmentModule
import com.kropotov.lovehate.di.mainscreen.fragments.NewOpinionDialogModule
import com.kropotov.lovehate.di.mainscreen.fragments.OpinionsFragmentModule
import com.kropotov.lovehate.di.mainscreen.fragments.TopicPageFragmentModule
import com.kropotov.lovehate.di.mainscreen.fragments.TopicsFragmentModule
import com.kropotov.lovehate.di.mainscreen.fragments.UsersFragmentModule
import com.kropotov.lovehate.ui.dialogs.NewOpinionDialog
import com.kropotov.lovehate.ui.dialogs.SendFeedbackDialog
import com.kropotov.lovehate.ui.screens.contribute.fragments.ContributeFragment
import com.kropotov.lovehate.ui.screens.favorites.FavoritesFragment
import com.kropotov.lovehate.ui.screens.feed.fragments.FeedFragment
import com.kropotov.lovehate.ui.screens.feed.fragments.OpinionsFragment
import com.kropotov.lovehate.ui.screens.profile.fragments.ProfileFragment
import com.kropotov.lovehate.ui.screens.ratings.fragments.RatingsFragment
import com.kropotov.lovehate.ui.screens.topicpage.fragments.TopicPageFragment
import com.kropotov.lovehate.ui.screens.topics.fragments.TopicsFragment
import com.kropotov.lovehate.ui.screens.topics.fragments.TopicsHostFragment
import com.kropotov.lovehate.ui.screens.users.UsersFragment
import com.kropotov.lovehate.ui.utilities.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [MainActivityModule.Injectors::class])
class MainActivityModule {

    @MainActivityScope
    @Provides
    fun provideApolloClient(sharedPrefs: SharedPreferencesHelper): ApolloClient
            = BackendMainService.create(sharedPrefs)

    @Module
    interface Injectors {
        @ContributesAndroidInjector(modules = [FeedFragmentModule::class])
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

        @ContributesAndroidInjector(modules = [NewOpinionDialogModule::class])
        fun injectNewOpinionDialog(): NewOpinionDialog

        @ContributesAndroidInjector
        fun injectSendFeedbackDialog(): SendFeedbackDialog

        @ContributesAndroidInjector
        fun injectRatingsFragment(): RatingsFragment

        @ContributesAndroidInjector
        fun injectProfileFragment(): ProfileFragment

        @ContributesAndroidInjector
        fun injectFavoritesFragment(): FavoritesFragment

        @ContributesAndroidInjector(modules = [UsersFragmentModule::class])
        fun injectUsersFragment(): UsersFragment
    }
}
