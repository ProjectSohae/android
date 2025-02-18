package com.jhw.sohae.domain.myinformation.usecase

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
import javax.inject.Inject

class MyInfoUseCase @Inject constructor(
    private val myInfoRepository: MyInfoRepository
) {

    fun getMyAccount(): Flow<MyAccountEntity> = flow {
        val response: Flow<MyAccountEntity>? = myInfoRepository.getMyAccount()

        if (response == null) {
            val myAccountEntity = MyAccountEntity(
                id = 0,
                realName = "",
                nickname = "",
                emailAddress = ""
            )

            updateMyAccount(myAccountEntity)
            emit(myAccountEntity)
        } else {
            response.collect {
                emit(it)
            }
        }
    }

    fun updateMyAccount(input: MyAccountEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.updateMyAccount(input)
        }
    }

    fun getMyWorkInfo(): Flow<MyWorkInfoEntity> = flow {
        val resposne =  myInfoRepository.getMyWorkInformation()

        if (resposne == null) {
            val myWorkInfoEntity = MyWorkInfoEntity(
                id = 0,
                workPlace = "",
                startWorkDay = -1,
                finishWorkDay = -1
            )

            updateMyWorkInfo(myWorkInfoEntity, false)
            emit(myWorkInfoEntity)
        } else {
            resposne.collect {
                emit(it)
            }
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
        myInfoRepository.getMyRank()?.collect {
            emit(it)
        }
    }

    fun updateMyRank(input: MyRankEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.updateMyRank(input)
        }
    }

    fun getMyWelfare(): Flow<MyWelfareEntity> = flow {
        myInfoRepository.getMyWelfare()?.collect {
            emit(it)
        }
    }

    fun updateMyWelfare(input: MyWelfareEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.updateMyWelfare(input)
        }
    }

    fun getMyLeave(): Flow<MyLeaveEntity> = flow {
        myInfoRepository.getMyLeave()?.collect {
            emit(it)
        }
    }

    fun updateMyLeave(input: MyLeaveEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            myInfoRepository.updateMyLeave(input)
        }
    }

    fun getMyUsedLeaveList(): Flow<List<MyUsedLeaveEntity>> = flow {
        myInfoRepository.getMyUsedLeaveList()?.collect {
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

    suspend fun getMyUsedLeaveListByLeaveKindIdx(leaveKindIdx: Int): List<MyUsedLeaveEntity> {
        lateinit var result: List<MyUsedLeaveEntity>

        runBlocking {
            myInfoRepository.getMyUsedLeaveListByLeaveKindIdx(leaveKindIdx)?.collect {
                result = it
            }
        }

        return result
    }

    suspend fun getMyUsedLeaveListByDate(startDate: Long, endDate: Long): List<MyUsedLeaveEntity> {
        lateinit var result: List<MyUsedLeaveEntity>

        runBlocking {
            myInfoRepository.getMyUsedLeaveListByDate(startDate, endDate)?.collect {
                result = it
            }
        }

        return result
    }
}