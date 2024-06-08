package com.aso.asomenuadmin

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        //plant timber debug tree
        Timber.plant(Timber.DebugTree())
    }
}