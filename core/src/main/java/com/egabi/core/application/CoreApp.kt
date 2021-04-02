package com.egabi.core.application

import android.app.Activity
import android.app.Application
import com.egabi.core.di.AppModule
import com.egabi.core.di.CoreComponent
import com.egabi.core.di.DaggerCoreComponent


open class CoreApp : Application() {

    companion object {
        lateinit var coreComponent: CoreComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDI()

    }

    var currentActivity: Activity? = null

    private fun initDI() {
        coreComponent = DaggerCoreComponent.builder().appModule(AppModule(this)).build()
    }
}