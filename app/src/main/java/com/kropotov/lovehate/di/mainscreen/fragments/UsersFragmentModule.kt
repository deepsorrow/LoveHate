package com.kropotov.lovehate.di.mainscreen.fragments

import com.kropotov.lovehate.data.UsersListType
import com.kropotov.lovehate.ui.screens.users.UsersFragment
import com.kropotov.lovehate.ui.utilities.serializable
import dagger.Module
import dagger.Provides

@Module
class UsersFragmentModule {

    @Provides
    fun provideUsersListType(fragment: UsersFragment) = fragment.arguments?.serializable(
        UsersFragment.USERS_LIST_TYPE_ARG,
        UsersListType::class.java
    ) ?: UsersListType.MOST_ACTIVE
}
