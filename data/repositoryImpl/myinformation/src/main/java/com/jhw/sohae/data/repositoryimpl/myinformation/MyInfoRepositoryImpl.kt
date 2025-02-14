package com.jhw.sohae.data.repositoryimpl.myinformation

import android.os.Build
import androidx.annotation.RequiresApi
import com.jhw.sohae.data.datasource.myinformation.dao.MyAccountDAO
import com.jhw.sohae.data.datasource.myinformation.dao.MyLeaveDAO
import com.jhw.sohae.data.datasource.myinformation.dao.MyRankDAO
import com.jhw.sohae.data.datasource.myinformation.dao.MyUsedLeaveDAO
import com.jhw.sohae.data.datasource.myinformation.dao.MyWelfareDAO
import com.jhw.sohae.data.datasource.myinformation.dao.MyWorkInformationDAO
import com.jhw.sohae.data.datasource.myinformation.database.MyInformationDBGraph
import com.jhw.sohae.data.model.myinformation.MyAccountDTO
import com.jhw.sohae.data.model.myinformation.MyLeaveDTO
import com.jhw.sohae.data.model.myinformation.MyRankDTO
import com.jhw.sohae.data.model.myinformation.MyWelfareDTO
import com.jhw.sohae.data.model.myinformation.MyWorkInformationDTO
import com.jhw.sohae.domain.myinformation.entity.MyAccountEntity
import com.jhw.sohae.domain.myinformation.entity.MyLeaveEntity
import com.jhw.sohae.domain.myinformation.entity.MyRankEntity
import com.jhw.sohae.domain.myinformation.entity.MyUsedLeaveEntity
import com.jhw.sohae.domain.myinformation.entity.MyWelfareEntity
import com.jhw.sohae.domain.myinformation.entity.MyWorkInfoEntity
import com.jhw.sohae.domain.myinformation.repository.MyInfoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object MyInfoRepositoryImpl: MyInfoRepository {

    private val myInfoDAO: MyAccountDAO = MyInformationDBGraph.myInformationDAO
    private val myWorkInfoDAO: MyWorkInformationDAO = MyInformationDBGraph.database.myWorkInformationDAO()
    private val myRankDAO: MyRankDAO = MyInformationDBGraph.database.myRankDAO()
    private val myWelfareDAO: MyWelfareDAO = MyInformationDBGraph.database.myWelfareDAO()
    private val myLeaveDAO: MyLeaveDAO = MyInformationDBGraph.database.myLeaveDAO()
    private val myUsedLeaveDAO: MyUsedLeaveDAO = MyInformationDBGraph.database.myUsedLeaveDAO()

    override fun initMyAccount(): Flow<MyAccountEntity> = flow {
        var myInfo: MyAccountDTO? = null

        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                myInfo = myInfoDAO.selectAll()

                if (myInfo == null) {
                    myInfo = MyAccountDTO(
                        realName = "",
                        nickname = "",
                        emailAddress = ""
                    )

                    myInfoDAO.insert(myInfo!!)
                }
            }.join()
        }

        emit(MyInfoMapper(myInfo!!))
    }

    override fun updateMyAccount(inputMyAccount: MyAccountEntity) {
        myInfoDAO.insert(MyInfoMapper(inputMyAccount))
    }

    override fun initMyWorkInformation(): Flow<MyWorkInfoEntity> = flow {
        var myWorkInfo: MyWorkInformationDTO? = null

        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                myWorkInfo = myWorkInfoDAO.selectAll()

                if (myWorkInfo == null) {
                    myWorkInfo = MyWorkInformationDTO(
                        workPlace = "",
                        startWorkDay = -1,
                        finishWorkDay = -1
                    )

                    myWorkInfoDAO.insert(myWorkInfo!!)
                }
            }.join()
        }

        emit(MyWorkInfoMapper(myWorkInfo!!))
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

    override fun initMyRank(): Flow<MyRankEntity> = flow {
        var myRank: MyRankDTO? = null

        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                myRank = myRankDAO.selectAll()

                if (myRank == null) {
                    myRank = MyRankDTO(
                        firstPromotionDay = -1,
                        secondPromotionDay = -1,
                        thirdPromotionDay = -1
                    )

                    myRankDAO.insert(myRank!!)
                }
            }.join()
        }

        emit(MyRankMapper(myRank!!))
    }

    override fun updateMyRank(inputMyRank: MyRankEntity) {
        myRankDAO.insert(MyRankMapper(inputMyRank))
    }

    override fun initMyWelfare(): Flow<MyWelfareEntity> = flow {
        var myWelfare: MyWelfareDTO? = null

        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                myWelfare = myWelfareDAO.selectAll()

                if (myWelfareDAO.selectAll() == null) {
                    myWelfare = MyWelfareDTO(
                        lunchSupport = 0,
                        transportationSupport = 0,
                        payday = 0
                    )

                    myWelfareDAO.insert(myWelfare!!)
                }
            }.join()
        }

        emit(MyWelfareMapper(myWelfare!!))
    }

    override fun updateMyWelfare(inputMyWelfare: MyWelfareEntity) {
        myWelfareDAO.insert(MyWelfareMapper(inputMyWelfare))
    }

    override fun initMyLeave(): Flow<MyLeaveEntity> = flow {
        var myLeave: MyLeaveDTO? = null

        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                myLeave = myLeaveDAO.selectAll()

                if (myLeave == null) {
                    myLeave = MyLeaveDTO(
                        firstAnnualLeave = -1,
                        secondAnnualLeave = -1,
                        sickLeave = -1
                    )

                    myLeaveDAO.insert(myLeave!!)
                }
            }.join()
        }

        emit(MyLeaveMapper(myLeave!!))
    }

    override fun updateMyLeave(inputMyLeave: MyLeaveEntity) {
        myLeaveDAO.insert(MyLeaveMapper(inputMyLeave))
    }

    override fun initMyUsedLeaveList(): Flow<List<MyUsedLeaveEntity>> = flow {
        var myUsedLeaveList: List<MyUsedLeaveEntity> = emptyList()

        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                val getMyUsedLeaveList = myUsedLeaveDAO.selectAll()

                if (getMyUsedLeaveList != null) {
                    myUsedLeaveList = getMyUsedLeaveList.map { MyUsedLeaveMapper(it) }
                }
            }.join()
        }

        flow { emit(myUsedLeaveList) }
    }

    override fun updateMyUsedLeave(inputMyUsedLeave: MyUsedLeaveEntity) {
        myUsedLeaveDAO.insert(MyUsedLeaveMapper(inputMyUsedLeave))
    }

    override fun deleteMyUsedLeave(id: Int) {
        myUsedLeaveDAO.deleteById(id)
    }

    override fun getMyUsedLeaveListByLeaveKindIdx(leaveKindIdx: Int): Flow<List<MyUsedLeaveEntity>> = flow {
        emit(
            myUsedLeaveDAO.selectByLeaveKindIdx(leaveKindIdx)?.map { MyUsedLeaveMapper(it) } ?: emptyList()
        )
    }

    override fun getMyUsedLeaveListByDate(startDate: Long, endDate: Long): Flow<List<MyUsedLeaveEntity>> = flow {
        emit(
            myUsedLeaveDAO.selectByDate(startDate, endDate)?.map { MyUsedLeaveMapper(it) } ?: emptyList()
        )
    }

}