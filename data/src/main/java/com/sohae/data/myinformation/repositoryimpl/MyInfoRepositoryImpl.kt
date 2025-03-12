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
import com.sohae.data.myinformation.mapper.MyAccountMapper
import com.sohae.data.myinformation.mapper.MyLeaveMapper
import com.sohae.data.myinformation.mapper.MyRankMapper
import com.sohae.data.myinformation.mapper.MySearchHistoryMapper
import com.sohae.data.myinformation.mapper.MyUsedLeaveMapper
import com.sohae.data.myinformation.mapper.MyWelfareMapper
import com.sohae.data.myinformation.mapper.MyWorkInfoMapper
import com.sohae.data.myinformation.mapper.toMyTokenDTO
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

object MyInfoRepositoryImpl : com.sohae.domain.myinformation.repository.MyInfoRepository {

    private val myInfoDB = MyInformationDBGraph.database
    private val myAccountDAO: MyAccountDAO = myInfoDB.myAccountDAO()
    private val myWorkInfoDAO: MyWorkInformationDAO = myInfoDB.myWorkInformationDAO()
    private val myRankDAO: MyRankDAO = myInfoDB.myRankDAO()
    private val myWelfareDAO: MyWelfareDAO = myInfoDB.myWelfareDAO()
    private val myLeaveDAO: MyLeaveDAO = myInfoDB.myLeaveDAO()
    private val myUsedLeaveDAO: MyUsedLeaveDAO = myInfoDB.myUsedLeaveDAO()
    private val mySearchHistoryDAO: MySearchHistoryDAO = myInfoDB.mySearchHistoryDAO()
    private val myTokenDAO: MyTokenDAO = myInfoDB.myTokenDAO()

    override fun getMyAccount(): Flow<com.sohae.domain.myinformation.entity.MyAccountEntity?> = flow {
        myAccountDAO.selectAll()
            .map {
                if (it != null) {
                    MyAccountMapper(it)
                } else { it }
            }
            .collect {
                emit(it)
            }
    }

    override fun updateMyAccount(inputMyAccount: com.sohae.domain.myinformation.entity.MyAccountEntity) {
        myAccountDAO.insert(MyAccountMapper(inputMyAccount))
    }

    override fun getMyWorkInformation(): Flow<com.sohae.domain.myinformation.entity.MyWorkInfoEntity?> = flow {
        myWorkInfoDAO.selectAll()
            .map {
                if (it != null) {
                    MyWorkInfoMapper(it)
                } else { it }
            }
            .collect {
                emit(it)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun updateMyWorkInformation(
        inputMyWorkInformation: com.sohae.domain.myinformation.entity.MyWorkInfoEntity,
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

    override fun getMyRank(): Flow<com.sohae.domain.myinformation.entity.MyRankEntity?> = flow {
        myRankDAO.selectAll()
            .map {
                if (it != null) {
                    MyRankMapper(it)
                } else { it }
            }
            .collect {
                emit(it)
            }
    }

    override fun updateMyRank(inputMyRank: com.sohae.domain.myinformation.entity.MyRankEntity) {
        myRankDAO.insert(MyRankMapper(inputMyRank))
    }

    override fun getMyWelfare(): Flow<com.sohae.domain.myinformation.entity.MyWelfareEntity?> = flow {
        myWelfareDAO.selectAll()
            .map {
                if (it != null) {
                    MyWelfareMapper(it)
                } else { it }
            }
            .collect {
                emit(it)
            }
    }

    override fun updateMyWelfare(inputMyWelfare: com.sohae.domain.myinformation.entity.MyWelfareEntity) {
        myWelfareDAO.insert(MyWelfareMapper(inputMyWelfare))
    }

    override fun getMyLeave(): Flow<com.sohae.domain.myinformation.entity.MyLeaveEntity?> = flow {
        myLeaveDAO.selectAll()
            .map {
                if (it != null) {
                    MyLeaveMapper(it)
                } else { it }
            }
            .collect {
                emit(it)
            }
    }

    override fun updateMyLeave(inputMyLeave: com.sohae.domain.myinformation.entity.MyLeaveEntity) {
        myLeaveDAO.insert(MyLeaveMapper(inputMyLeave))
    }

    override fun getAllMyUsedLeaveList(): Flow<List<com.sohae.domain.myinformation.entity.MyUsedLeaveEntity>?> = flow {
        myUsedLeaveDAO.selectAll()
            .map { myUsedLeaveList ->
                myUsedLeaveList?.map { MyUsedLeaveMapper(it) }
            }
            .collect {
                emit(it)
            }
    }

    override fun updateMyUsedLeave(inputMyUsedLeave: com.sohae.domain.myinformation.entity.MyUsedLeaveEntity) {
        myUsedLeaveDAO.insert(MyUsedLeaveMapper(inputMyUsedLeave))
    }

    override fun deleteMyUsedLeave(id: Int) {
        myUsedLeaveDAO.deleteById(id)
    }

    override fun getMyUsedLeaveListByLeaveKindIdx(leaveKindIdx: Int): List<com.sohae.domain.myinformation.entity.MyUsedLeaveEntity>? {
        return runBlocking {
            return@runBlocking myUsedLeaveDAO
                .selectByLeaveKindIdx(leaveKindIdx)?.map {
                    MyUsedLeaveMapper(it)
                }
        }
    }

    override fun getMyUsedLeaveListByDate(startDate: Long, endDate: Long): List<com.sohae.domain.myinformation.entity.MyUsedLeaveEntity>? {
        return runBlocking {
            return@runBlocking myUsedLeaveDAO
                .selectByDate(startDate, endDate)?.map {
                    MyUsedLeaveMapper(it)
                }
        }
    }

    override fun getAllMySearchHistory(): Flow<List<com.sohae.domain.myinformation.entity.MySearchHistoryEntity>?> = flow {
        mySearchHistoryDAO.selectAll()
            .map { mySearchHistoryList ->
                mySearchHistoryList?.map { MySearchHistoryMapper(it) }
            }
            .collect {
                emit(it)
            }
    }

    override fun updateMySearchHistory(input: com.sohae.domain.myinformation.entity.MySearchHistoryEntity) {
        mySearchHistoryDAO.insert(MySearchHistoryMapper(input))
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

    override fun setMyToken(input: com.sohae.domain.myinformation.entity.MyTokenEntity) {
        myTokenDAO.setMyToken(input.toMyTokenDTO())
    }
}