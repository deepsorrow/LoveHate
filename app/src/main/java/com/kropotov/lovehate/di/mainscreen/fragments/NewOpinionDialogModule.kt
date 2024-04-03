package com.kropotov.lovehate.di.mainscreen.fragments

import com.kropotov.lovehate.ui.dialogs.newopinion.NewOpinionDialog
import com.kropotov.lovehate.ui.screens.topicpage.fragments.TopicPageFragment
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class NewOpinionDialogModule {

    @Provides
    @Named(TopicPageFragment.TOPIC_PAGE_ID)
    fun provideTopicId(dialog: NewOpinionDialog) = dialog.arguments?.getInt(
        TopicPageFragment.TOPIC_PAGE_ID
    ) ?: 0
}
