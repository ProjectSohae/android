package com.jhw.sohae.data.myinformation.database

import android.content.Context
import androidx.room.Room

object MyInformationDBGraph {

    lateinit var database: MyInformationDB

    fun provide(context: Context) {
        database = Room.databaseBuilder(
            context,
            MyInformationDB::class.java, "my_information.db"
        ).build()
    }
}