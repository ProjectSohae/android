package com.jhw.sohae.activity

import android.app.Application
import com.jhw.sohae.BuildConfig
import com.jhw.sohae.data.datasource.myinformation.database.MyInformationDBGraph
import com.jhw.sohae.data.repositoryimpl.myinformation.MyInfoRepositoryImpl
import com.jhw.sohae.domain.myinformation.usecase.MyInfoUseCase
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InitApp: Application() {

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
        
        // remote local DB
        MyInformationDBGraph.provide(this)
        MyInfoUseCase.myInfoRepository = MyInfoRepositoryImpl
    }
}