package com.sohae.activity

import android.app.Application
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.sohae.BuildConfig
import com.sohae.data.myinformation.database.MyInformationDBGraph
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InitApp: Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(applicationContext);

        KakaoSdk.init(applicationContext, BuildConfig.KAKAO_NATIVE_KEY)
        
        // remote local DB
        MyInformationDBGraph.provide(applicationContext)
    }
}