package com.kropotov.lovehate

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import com.kropotov.lovehate.di.app.DaggerAppComponent
import com.kropotov.lovehate.di.app.AppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class LoveHateApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        initStrictMode()
        super.onCreate()

        appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
    }

    private fun initStrictMode() {
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectCustomSlowCalls()
                .detectNetwork()
                .penaltyLog()
                .build()
        )
    }

    override fun androidInjector() = dispatchingAndroidInjector
}
