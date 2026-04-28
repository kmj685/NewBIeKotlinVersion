package com.newBie.new_bie

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.newBie.new_bie.core.managers.CoilManager

class App : Application(), SingletonImageLoader.Factory {
    companion object{
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return CoilManager.create(
            context = instance
        )
    }
}