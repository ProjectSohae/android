package com.jhw.sohae.data.myinformation.repositoryimpl

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.jhw.sohae.data.myinformation.dao.MyAccountDAO
import com.jhw.sohae.data.myinformation.dao.MyLeaveDAO
import com.jhw.sohae.data.myinformation.dao.MyRankDAO
import com.jhw.sohae.data.myinformation.dao.MyUsedLeaveDAO
import com.jhw.sohae.data.myinformation.dao.MyWelfareDAO
import com.jhw.sohae.data.myinformation.dao.MyWorkInformationDAO
import com.jhw.sohae.data.myinformation.database.MyInformationDBGraph
import com.jhw.sohae.data.myinformation.dto.MyRankDTO
import com.jhw.sohae.data.myinformation.dto.MyWorkInformationDTO
import com.jhw.sohae.data.myinformation.mapper.MyAccountMapper
import com.jhw.sohae.data.myinformation.mapper.MyLeaveMapper
import com.jhw.sohae.data.myinformation.mapper.MyRankMapper
import com.jhw.sohae.data.myinformation.mapper.MyUsedLeaveMapper
import com.jhw.sohae.data.myinformation.mapper.MyWelfareMapper
import com.jhw.sohae.data.myinformation.mapper.MyWorkInfoMapper
import com.jhw.sohae.domain.myinformation.entity.MyAccountEntity
import com.jhw.sohae.domain.myinformation.entity.MyLeaveEntity
import com.jhw.sohae.domain.myinformation.entity.MyRankEntity
import com.jhw.sohae.domain.myinformation.entity.MyUsedLeaveEntity
import com.jhw.sohae.domain.myinformation.entity.MyWelfareEntity
import com.jhw.sohae.domain.myinformation.entity.MyWorkInfoEntity
import com.jhw.sohae.domain.myinformation.repository.MyInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object MyInfoRepositoryImpl : MyInfoRepository {

    private val myInfoDB = MyInformationDBGraph.database
    private val myAccountDAO: MyAccountDAO = myInfoDB.myAccountDAO()
    private val myWorkInfoDAO: MyWorkInformationDAO = myInfoDB.myWorkInformationDAO()
    private val myRankDAO: MyRankDAO = myInfoDB.myRankDAO()
    private val myWelfareDAO: MyWelfareDAO = myInfoDB.myWelfareDAO()
    private val myLeaveDAO: MyLeaveDAO = myInfoDB.myLeaveDAO()
    private val myUsedLeaveDAO: MyUsedLeaveDAO = myInfoDB.myUsedLeaveDAO()

    override fun getMyAccount(): Flow<MyAccountEntity>? {
        val myAccount = myAccountDAO.selectAll()?.map {
            MyAccountMapper(it)
        }

        return myAccount
    }

    override fun updateMyAccount(inputMyAccount: MyAccountEntity) {
        myAccountDAO.insert(MyAccountMapper(inputMyAccount))
    }

    override fun getMyWorkInformation(): Flow<MyWorkInfoEntity>? {
        val myWorkInfo = myWorkInfoDAO.selectAll()?.map {
            MyWorkInfoMapper(it)
        }

        return myWorkInfo
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun updateMyWorkInformation(
        inputMyWorkInformation: MyWorkInfoEntity,
        updateRelatedInfo: Boolean
    ) {
        var tmpMyWorkInfo = MyWorkInfoMapper(inputMyWorkInformation)

        if (updateRelatedInfo) {
            var startWorkDay = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(tmpMyWorkInfo.startWorkDay),
                ZoneId.systemDefault()
            )

            // 소집 해제일
            tmpMyWorkInfo = MyWorkInformationDTO(
                workPlace = tmpMyWorkInfo.workPlace,
                startWorkDay = tmpMyWorkInfo.startWorkDay,
                finishWorkDay = startWorkDay
                    .plusYears(1).plusMonths(9)
                    .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            )

            // 진급일
            startWorkDay = startWorkDay.minusDays( (startWorkDay.dayOfMonth - 1).toLong() )
            updateMyRank(
                MyRankMapper(
                    MyRankDTO(
                        firstPromotionDay = startWorkDay
                            .plusMonths(2)
                            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                        secondPromotionDay = startWorkDay
                            .plusMonths(8)
                            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                        thirdPromotionDay = startWorkDay
                            .plusMonths(14)
                            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    )
                )
            )
        }

        myWorkInfoDAO.insert(tmpMyWorkInfo)
    }

    override fun getMyRank(): Flow<MyRankEntity>? {
        val myRank = myRankDAO.selectAll()?.map {
            MyRankMapper(it)
        }

        return myRank
    }

    override fun updateMyRank(inputMyRank: MyRankEntity) {
        myRankDAO.insert(MyRankMapper(inputMyRank))
    }

    override fun getMyWelfare(): Flow<MyWelfareEntity>? {
        val myWelfare = myWelfareDAO.selectAll()?.map {
            MyWelfareMapper(it)
        }

        return myWelfare
    }

    override fun updateMyWelfare(inputMyWelfare: MyWelfareEntity) {
        myWelfareDAO.insert(MyWelfareMapper(inputMyWelfare))
    }

    override fun getMyLeave(): Flow<MyLeaveEntity>? {
        val myLeave = myLeaveDAO.selectAll()?.map {
            MyLeaveMapper(it)
        }

        return myLeave
    }

    override fun updateMyLeave(inputMyLeave: MyLeaveEntity) {
        myLeaveDAO.insert(MyLeaveMapper(inputMyLeave))
    }

    override fun getMyUsedLeaveList(): Flow<List<MyUsedLeaveEntity>> = flow {
        var myUsedLeaveList: List<MyUsedLeaveEntity> = emptyList()

        runBlocking {
            val getMyUsedLeaveList = myUsedLeaveDAO.selectAll()

            if (getMyUsedLeaveList != null) {
                myUsedLeaveList = getMyUsedLeaveList.map { MyUsedLeaveMapper(it) }
            }
        }

        emit(myUsedLeaveList)
    }

    override fun updateMyUsedLeave(inputMyUsedLeave: MyUsedLeaveEntity) {
        myUsedLeaveDAO.insert(MyUsedLeaveMapper(inputMyUsedLeave))
    }

    override fun deleteMyUsedLeave(id: Int) {
        myUsedLeaveDAO.deleteById(id)
    }

    override fun getMyUsedLeaveListByLeaveKindIdx(leaveKindIdx: Int): Flow<List<MyUsedLeaveEntity>> = flow {
        lateinit var myUsedLeaveList: List<MyUsedLeaveEntity>

        runBlocking {
            myUsedLeaveList = myUsedLeaveDAO
                .selectByLeaveKindIdx(leaveKindIdx)?.map {
                    MyUsedLeaveMapper(it)
                } ?: emptyList()
        }

        emit(myUsedLeaveList)
    }

    override fun getMyUsedLeaveListByDate(startDate: Long, endDate: Long): Flow<List<MyUsedLeaveEntity>> = flow {
        lateinit var myUsedLeaveList: List<MyUsedLeaveEntity>

        runBlocking {
            myUsedLeaveList = myUsedLeaveDAO
                .selectByDate(startDate, endDate)?.map {
                    MyUsedLeaveMapper(it)
                } ?: emptyList()
        }

        emit(myUsedLeaveList)
    }

}