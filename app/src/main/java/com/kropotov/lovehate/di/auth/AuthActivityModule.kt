package com.kropotov.lovehate.di.auth

import com.kropotov.lovehate.api.auth.BackendAuthService
import com.kropotov.lovehate.di.AuthActivityScope
import com.kropotov.lovehate.ui.AuthActivity
import com.kropotov.lovehate.ui.screens.auth.AuthRouter
import com.kropotov.lovehate.ui.screens.auth.fragments.LoginFragment
import com.kropotov.lovehate.ui.screens.auth.fragments.SignUpFragment
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [AuthActivityModule.Injectors::class])
class AuthActivityModule {

    @Provides
    @AuthActivityScope
    fun provideAuthClient(): BackendAuthService = BackendAuthService.create()

    @Provides
    fun provideRouter(activity: AuthActivity) = AuthRouter(activity.supportFragmentManager)

    @Module
    interface Injectors {

        @ContributesAndroidInjector
        fun injectLoginFragment(): LoginFragment

        @ContributesAndroidInjector
        fun injectRegistrationFragment(): SignUpFragment
    }
}
