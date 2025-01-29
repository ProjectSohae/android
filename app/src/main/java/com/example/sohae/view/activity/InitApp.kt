package com.example.sohae.view.activity

import android.app.Application
import com.example.sohae.model.data.myinformation.repository.MyInformationRepository

class InitApp: Application() {

    override fun onCreate() {
        super.onCreate()

        // remote local DB
        MyInformationRepository.provide(this)
    }
}