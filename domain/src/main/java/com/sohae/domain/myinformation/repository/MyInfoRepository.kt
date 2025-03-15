package com.sohae.domain.myinformation.repository

import com.sohae.domain.myinformation.entity.MyAccountEntity
import com.sohae.domain.myinformation.entity.MyLeaveEntity
import com.sohae.domain.myinformation.entity.MyRankEntity
import com.sohae.domain.myinformation.entity.MySearchHistoryEntity
import com.sohae.domain.myinformation.entity.MyTokenEntity
import com.sohae.domain.myinformation.entity.MyUsedLeaveEntity
import com.sohae.domain.myinformation.entity.MyWelfareEntity
import com.sohae.domain.myinformation.entity.MyWorkInfoEntity
import kotlinx.coroutines.flow.Flow

interface MyInfoRepository {

    fun getMyAccount(): Flow<MyAccountEntity?>
    fun updateMyAccount(inputMyAccount: MyAccountEntity)
    fun deleteMyAccount()

    fun getMyWorkInformation(): Flow<MyWorkInfoEntity?>
    fun updateMyWorkInformation(
        inputMyWorkInformation: MyWorkInfoEntity,
        updateRelatedInfo: Boolean
    )

    fun getMyRank(): Flow<MyRankEntity?>
    fun updateMyRank(inputMyRank: MyRankEntity)

    fun getMyWelfare(): Flow<MyWelfareEntity?>
    fun updateMyWelfare(inputMyWelfare: MyWelfareEntity)

    fun getMyLeave(): Flow<MyLeaveEntity?>
    fun updateMyLeave(inputMyLeave: MyLeaveEntity)

    fun getAllMyUsedLeaveList(): Flow<List<MyUsedLeaveEntity>?>
    fun updateMyUsedLeave(inputMyUsedLeave: MyUsedLeaveEntity)
    fun deleteMyUsedLeave(id: Int)

    fun getMyUsedLeaveListByLeaveKindIdx(leaveKindIdx: Int): List<MyUsedLeaveEntity>?
    fun getMyUsedLeaveListByDate(startDate: Long, endDate: Long): List<MyUsedLeaveEntity>?

    fun getAllMySearchHistory(): Flow<List<MySearchHistoryEntity>?>
    fun updateMySearchHistory(input: MySearchHistoryEntity)
    fun deleteAllMySearchHistory()
    fun deleteMySearchHistoryById(id: Int)

    fun getMyAccessToken(): Flow<String?>
    suspend fun getMyAccessTokenNotFlow(): String?
    fun getMyRefreshToken(): Flow<String?>
    suspend fun getMyRefreshTokenNotFlow(): String?
    fun updateMyToken(input: MyTokenEntity)
    fun deleteMyToken()
}