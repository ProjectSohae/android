package com.sohae.data.myinformation.repositoryimpl

import android.os.Build
import androidx.annotation.RequiresApi
import com.sohae.data.myinformation.dao.MyAccountDAO
import com.sohae.data.myinformation.dao.MyLeaveDAO
import com.sohae.data.myinformation.dao.MyRankDAO
import com.sohae.data.myinformation.dao.MySearchHistoryDAO
import com.sohae.data.myinformation.dao.MyTokenDAO
import com.sohae.data.myinformation.dao.MyUsedLeaveDAO
import com.sohae.data.myinformation.dao.MyWelfareDAO
import com.sohae.data.myinformation.dao.MyWorkInformationDAO
import com.sohae.data.myinformation.database.MyInformationDBGraph
import com.sohae.data.myinformation.dto.MyRankDTO
import com.sohae.data.myinformation.dto.MyWorkInformationDTO
import com.sohae.data.myinformation.mapper.toMyAccountDTO
import com.sohae.data.myinformation.mapper.toMyAccountEntity
import com.sohae.data.myinformation.mapper.toMyLeaveDTO
import com.sohae.data.myinformation.mapper.toMyLeaveEntity
import com.sohae.data.myinformation.mapper.toMyRankDTO
import com.sohae.data.myinformation.mapper.toMyRankEntity
import com.sohae.data.myinformation.mapper.toMySearchHistoryDTO
import com.sohae.data.myinformation.mapper.toMySearchHistoryEntity
import com.sohae.data.myinformation.mapper.toMyTokenDTO
import com.sohae.data.myinformation.mapper.toMyUsedLeaveDTO
import com.sohae.data.myinformation.mapper.toMyUsedLeaveEntity
import com.sohae.data.myinformation.mapper.toMyWelfareDTO
import com.sohae.data.myinformation.mapper.toMyWelfareEntity
import com.sohae.data.myinformation.mapper.toMyWorkInfoEntity
import com.sohae.data.myinformation.mapper.toMyWorkInformationDTO
import com.sohae.domain.myinformation.entity.MyAccountEntity
import com.sohae.domain.myinformation.entity.MyLeaveEntity
import com.sohae.domain.myinformation.entity.MyRankEntity
import com.sohae.domain.myinformation.entity.MySearchHistoryEntity
import com.sohae.domain.myinformation.entity.MyTokenEntity
import com.sohae.domain.myinformation.entity.MyUsedLeaveEntity
import com.sohae.domain.myinformation.entity.MyWelfareEntity
import com.sohae.domain.myinformation.entity.MyWorkInfoEntity
import com.sohae.domain.myinformation.repository.MyInfoRepository
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
    private val mySearchHistoryDAO: MySearchHistoryDAO = myInfoDB.mySearchHistoryDAO()
    private val myTokenDAO: MyTokenDAO = myInfoDB.myTokenDAO()

    override fun getMyAccount(): Flow<MyAccountEntity?> = flow {
        myAccountDAO.selectAll()
            .map {
                it?.toMyAccountEntity()
            }
            .collect {
                emit(it)
            }
    }

    override fun updateMyAccount(inputMyAccount: MyAccountEntity) {
        myAccountDAO.insert(inputMyAccount.toMyAccountDTO())
    }

    override fun deleteMyAccount() {
        myAccountDAO.deleteAll()
    }

    override fun getMyWorkInformation(): Flow<MyWorkInfoEntity?> = flow {
        myWorkInfoDAO.selectAll()
            .map {
                it?.toMyWorkInfoEntity()
            }
            .collect {
                emit(it)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun updateMyWorkInformation(
        inputMyWorkInformation: MyWorkInfoEntity,
        updateRelatedInfo: Boolean
    ) {
        var tmpMyWorkInfo = inputMyWorkInformation.toMyWorkInformationDTO()

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
                ).toMyRankEntity()
            )
        }

        myWorkInfoDAO.insert(tmpMyWorkInfo)
    }

    override fun getMyRank(): Flow<MyRankEntity?> = flow {
        myRankDAO.selectAll()
            .map {
                it?.toMyRankEntity()
            }
            .collect {
                emit(it)
            }
    }

    override fun updateMyRank(inputMyRank: MyRankEntity) {
        myRankDAO.insert(inputMyRank.toMyRankDTO())
    }

    override fun getMyWelfare(): Flow<MyWelfareEntity?> = flow {
        myWelfareDAO.selectAll()
            .map {
                it?.toMyWelfareEntity()
            }
            .collect {
                emit(it)
            }
    }

    override fun updateMyWelfare(inputMyWelfare: MyWelfareEntity) {
        myWelfareDAO.insert(inputMyWelfare.toMyWelfareDTO())
    }

    override fun getMyLeave(): Flow<MyLeaveEntity?> = flow {
        myLeaveDAO.selectAll()
            .map {
                it?.toMyLeaveEntity()
            }
            .collect {
                emit(it)
            }
    }

    override fun updateMyLeave(inputMyLeave: MyLeaveEntity) {
        myLeaveDAO.insert(inputMyLeave.toMyLeaveDTO())
    }

    override fun getAllMyUsedLeaveList(): Flow<List<MyUsedLeaveEntity>?> = flow {
        myUsedLeaveDAO.selectAll()
            .map { myUsedLeaveList ->
                myUsedLeaveList?.map { it.toMyUsedLeaveEntity() }
            }
            .collect {
                emit(it)
            }
    }

    override fun updateMyUsedLeave(inputMyUsedLeave: MyUsedLeaveEntity) {
        myUsedLeaveDAO.insert(inputMyUsedLeave.toMyUsedLeaveDTO())
    }

    override fun deleteMyUsedLeave(id: Int) {
        myUsedLeaveDAO.deleteById(id)
    }

    override fun getMyUsedLeaveListByLeaveKindIdx(leaveKindIdx: Int): List<MyUsedLeaveEntity>? {
        return runBlocking {
            return@runBlocking myUsedLeaveDAO
                .selectByLeaveKindIdx(leaveKindIdx)?.map {
                    it.toMyUsedLeaveEntity()
                }
        }
    }

    override fun getMyUsedLeaveListByDate(startDate: Long, endDate: Long): List<MyUsedLeaveEntity>? {
        return runBlocking {
            return@runBlocking myUsedLeaveDAO
                .selectByDate(startDate, endDate)?.map {
                    it.toMyUsedLeaveEntity()
                }
        }
    }

    override fun getAllMySearchHistory(): Flow<List<MySearchHistoryEntity>?> = flow {
        mySearchHistoryDAO.selectAll()
            .map { mySearchHistoryList ->
                mySearchHistoryList?.map { it.toMySearchHistoryEntity() }
            }
            .collect {
                emit(it)
            }
    }

    override fun updateMySearchHistory(input: MySearchHistoryEntity) {
        mySearchHistoryDAO.insert(input.toMySearchHistoryDTO())
    }

    override fun deleteAllMySearchHistory() {
        mySearchHistoryDAO.deleteAll()
    }

    override fun deleteMySearchHistoryById(id: Int) {
        mySearchHistoryDAO.deleteById(id)
    }

    override fun getMyAccessToken(): Flow<String?> = flow {
        myTokenDAO.getMyAccessToken().collect {
            emit(it)
        }
    }

    override fun getMyRefreshToken(): Flow<String?> = flow {
        myTokenDAO.getMyRefreshToken().collect {
            emit(it)
        }
    }

    override fun updateMyToken(input: MyTokenEntity) {
        myTokenDAO.setMyToken(input.toMyTokenDTO())
    }

    override fun deleteMyToken() {
        myTokenDAO.deleteAll()
    }
}