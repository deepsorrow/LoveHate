package com.kropotov.lovehate.di.app

import com.kropotov.lovehate.LoveHateApplication
import com.kropotov.lovehate.di.ActivityInjectorsModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ActivityInjectorsModule::class
    ]
)
@Singleton
interface AppComponent {

    fun inject(application: LoveHateApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: LoveHateApplication): Builder

        fun build(): AppComponent
    }
}
