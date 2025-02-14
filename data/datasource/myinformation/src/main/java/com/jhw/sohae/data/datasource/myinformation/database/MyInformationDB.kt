package com.jhw.sohae.data.datasource.myinformation.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jhw.sohae.data.datasource.myinformation.dao.MyAccountDAO
import com.jhw.sohae.data.datasource.myinformation.dao.MyLeaveDAO
import com.jhw.sohae.data.datasource.myinformation.dao.MyRankDAO
import com.jhw.sohae.data.datasource.myinformation.dao.MyUsedLeaveDAO
import com.jhw.sohae.data.datasource.myinformation.dao.MyWelfareDAO
import com.jhw.sohae.data.datasource.myinformation.dao.MyWorkInformationDAO
import com.jhw.sohae.data.model.myinformation.MyAccountDTO
import com.jhw.sohae.data.model.myinformation.MyLeaveDTO
import com.jhw.sohae.data.model.myinformation.MyRankDTO
import com.jhw.sohae.data.model.myinformation.MyUsedLeaveDTO
import com.jhw.sohae.data.model.myinformation.MyWelfareDTO
import com.jhw.sohae.data.model.myinformation.MyWorkInformationDTO

@Database(
    entities = [
        MyAccountDTO::class,
        MyWorkInformationDTO::class,
        MyRankDTO::class,
        MyWelfareDTO::class,
        MyLeaveDTO::class,
        MyUsedLeaveDTO::class ],
    version = 1,
    exportSchema = false
)
abstract class MyInformationDB: RoomDatabase() {
    abstract fun myAccountDAO(): MyAccountDAO

    abstract fun myWorkInformationDAO(): MyWorkInformationDAO

    abstract fun myRankDAO(): MyRankDAO

    abstract fun myWelfareDAO(): MyWelfareDAO

    abstract fun myLeaveDAO(): MyLeaveDAO

    abstract fun myUsedLeaveDAO(): MyUsedLeaveDAO
}