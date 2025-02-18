package com.jhw.sohae.domain.myinformation.repository

import com.jhw.sohae.domain.myinformation.entity.MyAccountEntity
import com.jhw.sohae.domain.myinformation.entity.MyLeaveEntity
import com.jhw.sohae.domain.myinformation.entity.MyRankEntity
import com.jhw.sohae.domain.myinformation.entity.MyUsedLeaveEntity
import com.jhw.sohae.domain.myinformation.entity.MyWelfareEntity
import com.jhw.sohae.domain.myinformation.entity.MyWorkInfoEntity
import kotlinx.coroutines.flow.Flow

interface MyInfoRepository {

    fun getMyAccount(): Flow<MyAccountEntity>?
    fun updateMyAccount(inputMyAccount: MyAccountEntity)

    fun getMyWorkInformation(): Flow<MyWorkInfoEntity>?
    fun updateMyWorkInformation(
        inputMyWorkInformation: MyWorkInfoEntity,
        updateRelatedInfo: Boolean
    )

    fun getMyRank(): Flow<MyRankEntity>?
    fun updateMyRank(inputMyRank: MyRankEntity)

    fun getMyWelfare(): Flow<MyWelfareEntity>?
    fun updateMyWelfare(inputMyWelfare: MyWelfareEntity)

    fun getMyLeave(): Flow<MyLeaveEntity>?
    fun updateMyLeave(inputMyLeave: MyLeaveEntity)

    fun getMyUsedLeaveList(): Flow<List<MyUsedLeaveEntity>>?
    fun updateMyUsedLeave(inputMyUsedLeave: MyUsedLeaveEntity)
    fun deleteMyUsedLeave(id: Int)

    fun getMyUsedLeaveListByLeaveKindIdx(leaveKindIdx: Int): Flow<List<MyUsedLeaveEntity>>?
    fun getMyUsedLeaveListByDate(startDate: Long, endDate: Long): Flow<List<MyUsedLeaveEntity>>?
}