package com.example.gongik.view.activity

import android.app.Application
import com.example.gongik.model.repository.MyInformationRepository

class InitApp: Application() {

    override fun onCreate() {
        super.onCreate()

        // remote local DB
        MyInformationRepository.provide(this)
    }
}