package com.jhw.sohae.data.myinformation.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jhw.sohae.data.myinformation.dao.MyAccountDAO
import com.jhw.sohae.data.myinformation.dao.MyLeaveDAO
import com.jhw.sohae.data.myinformation.dao.MyRankDAO
import com.jhw.sohae.data.myinformation.dao.MyUsedLeaveDAO
import com.jhw.sohae.data.myinformation.dao.MyWelfareDAO
import com.jhw.sohae.data.myinformation.dao.MyWorkInformationDAO
import com.jhw.sohae.data.myinformation.dto.MyAccountDTO
import com.jhw.sohae.data.myinformation.dto.MyLeaveDTO
import com.jhw.sohae.data.myinformation.dto.MyRankDTO
import com.jhw.sohae.data.myinformation.dto.MyUsedLeaveDTO
import com.jhw.sohae.data.myinformation.dto.MyWelfareDTO
import com.jhw.sohae.data.myinformation.dto.MyWorkInformationDTO

@Database(
    entities = [
        com.jhw.sohae.data.myinformation.dto.MyAccountDTO::class,
        com.jhw.sohae.data.myinformation.dto.MyWorkInformationDTO::class,
        com.jhw.sohae.data.myinformation.dto.MyRankDTO::class,
        com.jhw.sohae.data.myinformation.dto.MyWelfareDTO::class,
        com.jhw.sohae.data.myinformation.dto.MyLeaveDTO::class,
        com.jhw.sohae.data.myinformation.dto.MyUsedLeaveDTO::class ],
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