package com.example.gongik.model.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gongik.model.converter.DateConverter
import com.example.gongik.model.data.myinformation.MyInformation
import com.example.gongik.model.data.myinformation.MyLeave
import com.example.gongik.model.data.myinformation.MyRank
import com.example.gongik.model.data.myinformation.MyUsedLeave
import com.example.gongik.model.data.myinformation.MyWelfare
import com.example.gongik.model.data.myinformation.MyWorkInformation

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
@TypeConverters(DateConverter::class)
abstract class MyInformationDB: RoomDatabase() {
    abstract fun myInformationDAO(): MyInformationDAO

    abstract fun myWorkInformationDAO(): MyWorkInformationDAO

    abstract fun myRankDAO(): MyRankDAO

    abstract fun myWelfareDAO(): MyWelfareDAO

    abstract fun myLeaveDAO(): MyLeaveDAO

    abstract fun myUsedLeaveDAO(): MyUsedLeaveDAO
}