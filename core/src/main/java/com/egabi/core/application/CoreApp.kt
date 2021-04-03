package com.egabi.core.application

import android.app.Activity
import android.app.Application
import android.content.Context
import com.egabi.core.di.AppModule
import com.egabi.core.di.CoreComponent
import com.egabi.core.di.DaggerCoreComponent


open class CoreApp : Application() {

    companion object {
        var mContext: Context? = null
        fun getContext(): Context {
           return mContext!!
        }

        lateinit var coreComponent: CoreComponent
    }

   open fun getContext(): Context? {
        return mContext
    }
    override fun onCreate() {
        super.onCreate()
        initDI()
       mContext = this

    }

    var currentActivity: Activity? = null

    private fun initDI() {
        coreComponent = DaggerCoreComponent.builder().appModule(AppModule(this)).build()
    }
}