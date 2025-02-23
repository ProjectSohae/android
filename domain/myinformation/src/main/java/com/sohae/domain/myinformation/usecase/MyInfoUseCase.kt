package com.sohae.domain.myinformation.usecase

import com.sohae.domain.myinformation.entity.MyAccountEntity
import com.sohae.domain.myinformation.entity.MyLeaveEntity
import com.sohae.domain.myinformation.entity.MyRankEntity
import com.sohae.domain.myinformation.entity.MySearchHistoryEntity
import com.sohae.domain.myinformation.entity.MyUsedLeaveEntity
import com.sohae.domain.myinformation.entity.MyWelfareEntity
import com.sohae.domain.myinformation.entity.MyWorkInfoEntity
import com.sohae.domain.myinformation.repository.MyInfoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MyInfoUseCase @Inject constructor(
    private val myInfoRepository: MyInfoRepository
) {

    fun getMyAccount(): Flow<MyAccountEntity> = flow {
        myInfoRepository.getMyAccount()
            .mapNotNull {
                it ?: MyAccountEntity(
                    id = 0,
                    realName = "",
                    nickname = "",
                    emailAddress = ""
                )
            }
            .collect {
                emit(it)
            }
    }

    fun updateMyAccount(input: MyAccountEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.updateMyAccount(input)
        }
    }

    fun getMyWorkInfo(): Flow<MyWorkInfoEntity> = flow {
        myInfoRepository.getMyWorkInformation()
            .mapNotNull {
                it ?: MyWorkInfoEntity(
                    id = 0,
                    workPlace = "",
                    startWorkDay = -1,
                    finishWorkDay = -1
                )
            }
            .collect {
                emit(it)
            }
    }

    fun updateMyWorkInfo(
        input: MyWorkInfoEntity,
        updateRelatedInfo: Boolean
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.updateMyWorkInformation(input, updateRelatedInfo)
        }
    }

    fun getMyRank(): Flow<MyRankEntity> = flow {
        myInfoRepository.getMyRank()
            .mapNotNull {
                it ?: MyRankEntity(
                    id = 0,
                    firstPromotionDay = -1,
                    secondPromotionDay = -1,
                    thirdPromotionDay = -1
                )
            }
            .collect {
                emit(it)
            }
    }

    fun updateMyRank(input: MyRankEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.updateMyRank(input)
        }
    }

    fun getMyWelfare(): Flow<MyWelfareEntity> = flow {
        myInfoRepository.getMyWelfare()
            .mapNotNull {
                it ?: MyWelfareEntity(
                    id = 0,
                    lunchSupport = 0,
                    transportationSupport = 0,
                    payday = -1
                )
            }
            .collect {
                emit(it)
            }
    }

    fun updateMyWelfare(input: MyWelfareEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.updateMyWelfare(input)
        }
    }

    fun getMyLeave(): Flow<MyLeaveEntity> = flow {
        myInfoRepository.getMyLeave()
            .mapNotNull {
                it ?: MyLeaveEntity(
                    id = 0,
                    firstAnnualLeave = -1,
                    secondAnnualLeave = -1,
                    sickLeave = -1
                )
            }
            .collect {
                emit(it)
            }
    }

    fun updateMyLeave(input: MyLeaveEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.updateMyLeave(input)
        }
    }

    fun getAllMyUsedLeaveList(): Flow<List<MyUsedLeaveEntity>> = flow {
        myInfoRepository.getAllMyUsedLeaveList()
            .mapNotNull {
                it ?: emptyList()
            }
            .collect {
                emit(it)
            }
    }

    fun updateMyUsedLeave(inputMyUsedLeave: MyUsedLeaveEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.updateMyUsedLeave(inputMyUsedLeave)
        }
    }

    fun deleteMyUsedLeave(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.deleteMyUsedLeave(id)
        }
    }

    fun getMyUsedLeaveListByLeaveKindIdx(leaveKindIdx: Int): List<MyUsedLeaveEntity> {
        return myInfoRepository.getMyUsedLeaveListByLeaveKindIdx(leaveKindIdx) ?: emptyList()
    }

    fun getMyUsedLeaveListByDate(startDate: Long, endDate: Long): List<MyUsedLeaveEntity> {
        return myInfoRepository.getMyUsedLeaveListByDate(startDate, endDate) ?: emptyList()
    }

    fun getAllMySearchHistoryList(): Flow<List<MySearchHistoryEntity>> = flow {
        myInfoRepository.getAllMySearchHistory()
            .mapNotNull {
                it ?: emptyList()
            }
            .collect {
                emit(it)
            }
    }

    fun updateMySearchHistory(input: MySearchHistoryEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.updateMySearchHistory(input)
        }
    }

    fun deleteAllMySearchHistory() {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.deleteAllMySearchHistory()
        }
    }

    fun deleteMySearchHistoryById(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.deleteMySearchHistoryById(id)
        }
    }
}