package com.sohae.activity

import android.app.Application
import com.sohae.BuildConfig
import com.sohae.data.myinformation.database.MyInformationDBGraph
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InitApp: Application() {

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
        
        // remote local DB
        MyInformationDBGraph.provide(this)
    }
}