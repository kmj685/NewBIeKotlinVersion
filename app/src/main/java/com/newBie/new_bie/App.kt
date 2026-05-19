package com.newBie.new_bie

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.newBie.new_bie.core.managers.CoilManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // 힐트가 앱 전체의 의존성 생명주기를 관리하도록 선언
class App : Application(), SingletonImageLoader.Factory {
    companion object {
        private var _instance: App? = null
        val instance: App get() = _instance ?: throw IllegalStateException("App is not initialized")

        // 필요할 때 어디서든 Context를 편하게 쓰기 위한 헬퍼
        val context: PlatformContext get() = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        _instance = this
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        // instance 대신 매개변수로 들어온 context를 사용하는 것이 더 권장
        return CoilManager.create(context = context)
    }
}