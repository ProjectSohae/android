package com.example.sohae.model.data.myinformation.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sohae.model.data.myinformation.MyInformation
import com.example.sohae.model.data.myinformation.MyLeave
import com.example.sohae.model.data.myinformation.MyRank
import com.example.sohae.model.data.myinformation.MyUsedLeave
import com.example.sohae.model.data.myinformation.MyWelfare
import com.example.sohae.model.data.myinformation.MyWorkInformation

@Database(
    entities = [
        MyInformation::class,
        MyWorkInformation::class,
        MyRank::class,
        MyWelfare::class,
        MyLeave::class,
        MyUsedLeave::class ],
    version = 1,
    exportSchema = false
)
abstract class MyInformationDB: RoomDatabase() {
    abstract fun myInformationDAO(): MyInformationDAO

    abstract fun myWorkInformationDAO(): MyWorkInformationDAO

    abstract fun myRankDAO(): MyRankDAO

    abstract fun myWelfareDAO(): MyWelfareDAO

    abstract fun myLeaveDAO(): MyLeaveDAO

    abstract fun myUsedLeaveDAO(): MyUsedLeaveDAO
}