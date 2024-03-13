package com.kropotov.lovehate.di.modules.fragments

import com.kropotov.lovehate.data.OpinionSortType
import com.kropotov.lovehate.ui.fragments.OpinionsFragment
import com.kropotov.lovehate.ui.utilities.serializable
import dagger.Module
import dagger.Provides

@Module
class OpinionsFragmentModule {

    @Provides
    fun provideSortType(fragment: OpinionsFragment) = fragment.arguments?.serializable(
        OpinionsFragment.OPINION_TYPE_TAB_ARG,
        OpinionSortType::class.java
    ) ?: OpinionSortType.UNION
}