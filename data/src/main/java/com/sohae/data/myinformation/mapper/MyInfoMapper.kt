package com.sohae.data.myinformation.mapper

import com.sohae.data.myinformation.dto.MyAccountDTO
import com.sohae.data.myinformation.dto.MyLeaveDTO
import com.sohae.data.myinformation.dto.MyRankDTO
import com.sohae.data.myinformation.dto.MySearchHistoryDTO
import com.sohae.data.myinformation.dto.MyTokenDTO
import com.sohae.data.myinformation.dto.MyUsedLeaveDTO
import com.sohae.data.myinformation.dto.MyWelfareDTO
import com.sohae.data.myinformation.dto.MyWorkInformationDTO
import com.sohae.domain.myinformation.entity.MyAccountEntity
import com.sohae.domain.myinformation.entity.MyLeaveEntity
import com.sohae.domain.myinformation.entity.MyRankEntity
import com.sohae.domain.myinformation.entity.MySearchHistoryEntity
import com.sohae.domain.myinformation.entity.MyTokenEntity
import com.sohae.domain.myinformation.entity.MyUsedLeaveEntity
import com.sohae.domain.myinformation.entity.MyWelfareEntity
import com.sohae.domain.myinformation.entity.MyWorkInfoEntity
import java.util.UUID

fun MyAccountDTO.toMyAccountEntity(): MyAccountEntity =
    MyAccountEntity(
        id = UUID.fromString(this.id),
        username = this.username,
        emailAddress = this.emailAddress
    )

fun MyAccountEntity.toMyAccountDTO(): MyAccountDTO =
    MyAccountDTO(
        id = this.id.toString(),
        username = this.username,
        emailAddress = this.emailAddress
    )

fun MyLeaveDTO.toMyLeaveEntity(): MyLeaveEntity =
    MyLeaveEntity(
        this.id,
        this.firstAnnualLeave,
        this.secondAnnualLeave,
        this.sickLeave
    )

fun MyLeaveEntity.toMyLeaveDTO(): MyLeaveDTO =
    MyLeaveDTO(
        this.id,
        this.firstAnnualLeave,
        this.secondAnnualLeave,
        this.sickLeave
    )

fun MyRankDTO.toMyRankEntity(): MyRankEntity =
    MyRankEntity(
        this.id,
        this.firstPromotionDay,
        this.secondPromotionDay,
        this.thirdPromotionDay
    )

fun MyRankEntity.toMyRankDTO(): MyRankDTO =
    MyRankDTO(
        this.id,
        this.firstPromotionDay,
        this.secondPromotionDay,
        this.thirdPromotionDay
    )

fun MyUsedLeaveDTO.toMyUsedLeaveEntity(): MyUsedLeaveEntity =
    MyUsedLeaveEntity(
        this.id,
        this.leaveKindIdx,
        this.leaveTypeIdx,
        this.reason,
        this.usedLeaveTime,
        this.leaveStartDate,
        this.leaveEndDate,
        this.receiveLunchSupport,
        this.receiveTransportationSupport
    )

fun MyUsedLeaveEntity.toMyUsedLeaveDTO(): MyUsedLeaveDTO =
    MyUsedLeaveDTO(
        this.id,
        this.leaveKindIdx,
        this.leaveTypeIdx,
        this.reason,
        this.usedLeaveTime,
        this.leaveStartDate,
        this.leaveEndDate,
        this.receiveLunchSupport,
        this.receiveTransportationSupport
    )

fun MyWelfareDTO.toMyWelfareEntity(): MyWelfareEntity =
    MyWelfareEntity(
        this.id,
        this.lunchSupport,
        this.transportationSupport,
        this.payday
    )

fun MyWelfareEntity.toMyWelfareDTO(): MyWelfareDTO =
    MyWelfareDTO(
        this.id,
        this.lunchSupport,
        this.transportationSupport,
        this.payday
    )

fun MyWorkInformationDTO.toMyWorkInfoEntity(): MyWorkInfoEntity =
    MyWorkInfoEntity(
        this.id,
        this.workPlace,
        this.startWorkDay,
        this.finishWorkDay
    )

fun MyWorkInfoEntity.toMyWorkInformationDTO(): MyWorkInformationDTO =
    MyWorkInformationDTO(
        this.id,
        this.workPlace,
        this.startWorkDay,
        this.finishWorkDay
    )

fun MySearchHistoryDTO.toMySearchHistoryEntity(): MySearchHistoryEntity =
    MySearchHistoryEntity(
        this.id,
        this.keyword
    )

fun MySearchHistoryEntity.toMySearchHistoryDTO(): MySearchHistoryDTO =
    MySearchHistoryDTO(
        this.id,
        this.keyword
    )

fun MyTokenDTO.toMyTokenEntity(): MyTokenEntity =
    MyTokenEntity(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )

fun MyTokenEntity.toMyTokenDTO(): MyTokenDTO = MyTokenDTO(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken
)