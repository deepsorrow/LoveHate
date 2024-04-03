package com.kropotov.lovehate.di.mainscreen

import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.api.main.BackendMainService
import com.kropotov.lovehate.di.MainActivityScope
import com.kropotov.lovehate.di.mainscreen.fragments.AttachmentViewerFragmentModule
import com.kropotov.lovehate.di.mainscreen.fragments.FeedFragmentModule
import com.kropotov.lovehate.di.mainscreen.fragments.NewOpinionDialogModule
import com.kropotov.lovehate.di.mainscreen.fragments.OpinionsFragmentModule
import com.kropotov.lovehate.di.mainscreen.fragments.TopicPageFragmentModule
import com.kropotov.lovehate.di.mainscreen.fragments.TopicsFragmentModule
import com.kropotov.lovehate.di.mainscreen.fragments.UsersFragmentModule
import com.kropotov.lovehate.ui.screens.attachmentviewer.AttachmentViewerFragment
import com.kropotov.lovehate.ui.dialogs.newopinion.NewOpinionDialog
import com.kropotov.lovehate.ui.dialogs.pickmedia.PickMediaDialog
import com.kropotov.lovehate.ui.dialogs.sendfeedback.SendFeedbackDialog
import com.kropotov.lovehate.ui.dialogs.newtopic.NewTopicDialog
import com.kropotov.lovehate.ui.screens.favorites.FavoritesFragment
import com.kropotov.lovehate.ui.screens.opinions.fragments.OpinionsHostFragment
import com.kropotov.lovehate.ui.screens.opinions.fragments.OpinionsFragment
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
            = BackendMainService.createApolloClient(sharedPrefs)

    @MainActivityScope
    @Provides
    fun provideMainService(sharedPrefs: SharedPreferencesHelper): BackendMainService
            = BackendMainService.createMainService(sharedPrefs)

    @Module
    interface Injectors {
        @ContributesAndroidInjector(modules = [FeedFragmentModule::class])
        fun injectFeedFragment(): OpinionsHostFragment

        @ContributesAndroidInjector(modules = [OpinionsFragmentModule::class])
        fun injectOpinionsFragment(): OpinionsFragment

        @ContributesAndroidInjector
        fun injectTopicsHostScreenFragment(): TopicsHostFragment

        @ContributesAndroidInjector(modules = [TopicsFragmentModule::class])
        fun injectTopicsCategoryFragment(): TopicsFragment

        @ContributesAndroidInjector
        fun injectContributeFragment(): NewTopicDialog

        @ContributesAndroidInjector(modules = [TopicPageFragmentModule::class])
        fun injectTopicPageFragment(): TopicPageFragment

        @ContributesAndroidInjector(modules = [NewOpinionDialogModule::class])
        fun injectNewOpinionDialog(): NewOpinionDialog

        @ContributesAndroidInjector
        fun injectPickMediaDialog(): PickMediaDialog

        @ContributesAndroidInjector
        fun injectSendFeedbackDialog(): SendFeedbackDialog

        @ContributesAndroidInjector
        fun injectRatingsFragment(): RatingsFragment

        @ContributesAndroidInjector
        fun injectProfileFragment(): ProfileFragment

        @ContributesAndroidInjector
        fun injectFavoritesFragment(): FavoritesFragment

        @ContributesAndroidInjector(modules = [AttachmentViewerFragmentModule::class])
        fun injectAttachmentViewerFragment(): AttachmentViewerFragment

        @ContributesAndroidInjector(modules = [UsersFragmentModule::class])
        fun injectUsersFragment(): UsersFragment
    }
}
