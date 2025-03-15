package com.sohae.domain.myinformation.usecase

import com.sohae.domain.myinformation.entity.MyAccountEntity
import com.sohae.domain.myinformation.entity.MyLeaveEntity
import com.sohae.domain.myinformation.entity.MyRankEntity
import com.sohae.domain.myinformation.entity.MySearchHistoryEntity
import com.sohae.domain.myinformation.entity.MyTokenEntity
import com.sohae.domain.myinformation.entity.MyUsedLeaveEntity
import com.sohae.domain.myinformation.entity.MyWelfareEntity
import com.sohae.domain.myinformation.entity.MyWorkInfoEntity
import com.sohae.domain.myinformation.repository.MyInfoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyInfoUseCase @Inject constructor(
    private val myInfoRepository: MyInfoRepository
) {

    fun getMyAccount(): Flow<MyAccountEntity?> = flow {
        myInfoRepository.getMyAccount().collect {
            emit(it)
        }
    }

    fun updateMyAccount(input: MyAccountEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.updateMyAccount(input)
        }
    }

    fun deleteMyAccount() {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.deleteMyAccount()
        }
    }

    fun getMyWorkInfo(): Flow<MyWorkInfoEntity?> = flow {
        myInfoRepository.getMyWorkInformation().collect {
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

    fun getMyRank(): Flow<MyRankEntity?> = flow {
        myInfoRepository.getMyRank().collect {
            emit(it)
        }
    }

    fun updateMyRank(input: MyRankEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.updateMyRank(input)
        }
    }

    fun getMyWelfare(): Flow<MyWelfareEntity?> = flow {
        myInfoRepository.getMyWelfare().collect {
            emit(it)
        }
    }

    fun updateMyWelfare(input: MyWelfareEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.updateMyWelfare(input)
        }
    }

    fun getMyLeave(): Flow<MyLeaveEntity?> = flow {
        myInfoRepository.getMyLeave().collect {
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

    fun getMyAccessToken(): Flow<String?> = flow {
        myInfoRepository.getMyAccessToken().collect {
            emit(it)
        }
    }

    suspend fun getMyAccessTokenNotFlow(): String? {
        return myInfoRepository.getMyAccessTokenNotFlow()
    }

    fun getMyRefreshToken(): Flow<String?> = flow {
        myInfoRepository.getMyRefreshToken().collect {
            emit(it)
        }
    }

    suspend fun getMyRefreshTokenNotFlow(): String? {
        return myInfoRepository.getMyRefreshTokenNotFlow()
    }

    fun updateMyToken(input: MyTokenEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.updateMyToken(input)
        }
    }

    fun deleteMyToken() {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.deleteMyToken()
        }
    }
}