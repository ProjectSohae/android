package com.example.sohae.view.activity

import android.app.Application
import com.example.sohae.BuildConfig
import com.example.sohae.model.data.myinformation.repository.MyInformationRepository
import com.kakao.sdk.common.KakaoSdk

class InitApp: Application() {

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
        
        // remote local DB
        MyInformationRepository.provide(this)
    }
}