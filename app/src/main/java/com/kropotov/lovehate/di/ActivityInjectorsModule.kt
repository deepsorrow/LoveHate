@file:Suppress("unused")

package com.kropotov.lovehate.di

import com.kropotov.lovehate.di.auth.AuthActivityModule
import com.kropotov.lovehate.di.mainscreen.MainActivityModule
import com.kropotov.lovehate.ui.AuthActivity
import com.kropotov.lovehate.ui.MainScreenActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityInjectorsModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    @MainActivityScope
    fun injectMainActivity(): MainScreenActivity

    @ContributesAndroidInjector(modules = [AuthActivityModule::class])
    @AuthActivityScope
    fun injectAuthActivity(): AuthActivity
}
