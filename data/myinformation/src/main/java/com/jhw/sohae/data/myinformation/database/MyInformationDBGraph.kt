package com.jhw.sohae.data.myinformation.database

import android.content.Context
import androidx.room.Room

object MyInformationDBGraph {

    // lateinit var <- 나중에 값을 초기화 할 것임을 선언
    lateinit var database: MyInformationDB

    // by lazy <- 첫 호출 시에 람다 실행하여 초기 값 설정
    val myInformationDAO by lazy {
        database.myAccountDAO()
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(
            context,
            MyInformationDB::class.java, "my_information.db"
        ).build()
    }
}