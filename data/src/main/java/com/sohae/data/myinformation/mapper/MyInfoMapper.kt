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

fun MyAccountMapper(input: MyAccountDTO): com.sohae.domain.myinformation.entity.MyAccountEntity =
    com.sohae.domain.myinformation.entity.MyAccountEntity(
        input.id,
        input.realName,
        input.nickname,
        input.emailAddress
    )

fun MyAccountMapper(input: com.sohae.domain.myinformation.entity.MyAccountEntity): MyAccountDTO =
    MyAccountDTO(
        input.id,
        input.realName,
        input.nickname,
        input.emailAddress
    )

fun MyLeaveMapper(input: MyLeaveDTO): com.sohae.domain.myinformation.entity.MyLeaveEntity =
    com.sohae.domain.myinformation.entity.MyLeaveEntity(
        input.id,
        input.firstAnnualLeave,
        input.secondAnnualLeave,
        input.sickLeave
    )

fun MyLeaveMapper(input: com.sohae.domain.myinformation.entity.MyLeaveEntity): MyLeaveDTO =
    MyLeaveDTO(
        input.id,
        input.firstAnnualLeave,
        input.secondAnnualLeave,
        input.sickLeave
    )

fun MyRankMapper(input: MyRankDTO): com.sohae.domain.myinformation.entity.MyRankEntity =
    com.sohae.domain.myinformation.entity.MyRankEntity(
        input.id,
        input.firstPromotionDay,
        input.secondPromotionDay,
        input.thirdPromotionDay
    )

fun MyRankMapper(input: com.sohae.domain.myinformation.entity.MyRankEntity): MyRankDTO =
    MyRankDTO(
        input.id,
        input.firstPromotionDay,
        input.secondPromotionDay,
        input.thirdPromotionDay
    )

fun MyUsedLeaveMapper(input: MyUsedLeaveDTO): com.sohae.domain.myinformation.entity.MyUsedLeaveEntity =
    com.sohae.domain.myinformation.entity.MyUsedLeaveEntity(
        input.id,
        input.leaveKindIdx,
        input.leaveTypeIdx,
        input.reason,
        input.usedLeaveTime,
        input.leaveStartDate,
        input.leaveEndDate,
        input.receiveLunchSupport,
        input.receiveTransportationSupport
    )

fun MyUsedLeaveMapper(input: com.sohae.domain.myinformation.entity.MyUsedLeaveEntity): MyUsedLeaveDTO =
    MyUsedLeaveDTO(
        input.id,
        input.leaveKindIdx,
        input.leaveTypeIdx,
        input.reason,
        input.usedLeaveTime,
        input.leaveStartDate,
        input.leaveEndDate,
        input.receiveLunchSupport,
        input.receiveTransportationSupport
    )

fun MyWelfareMapper(input: MyWelfareDTO): com.sohae.domain.myinformation.entity.MyWelfareEntity =
    com.sohae.domain.myinformation.entity.MyWelfareEntity(
        input.id,
        input.lunchSupport,
        input.transportationSupport,
        input.payday
    )

fun MyWelfareMapper(input: com.sohae.domain.myinformation.entity.MyWelfareEntity): MyWelfareDTO =
    MyWelfareDTO(
        input.id,
        input.lunchSupport,
        input.transportationSupport,
        input.payday
    )

fun MyWorkInfoMapper(input: MyWorkInformationDTO): com.sohae.domain.myinformation.entity.MyWorkInfoEntity =
    com.sohae.domain.myinformation.entity.MyWorkInfoEntity(
        input.id,
        input.workPlace,
        input.startWorkDay,
        input.finishWorkDay
    )

fun MyWorkInfoMapper(input: com.sohae.domain.myinformation.entity.MyWorkInfoEntity): MyWorkInformationDTO =
    MyWorkInformationDTO(
        input.id,
        input.workPlace,
        input.startWorkDay,
        input.finishWorkDay
    )

fun MySearchHistoryMapper(input: MySearchHistoryDTO): com.sohae.domain.myinformation.entity.MySearchHistoryEntity =
    com.sohae.domain.myinformation.entity.MySearchHistoryEntity(
        input.id,
        input.keyword
    )

fun MySearchHistoryMapper(input: com.sohae.domain.myinformation.entity.MySearchHistoryEntity): MySearchHistoryDTO =
    MySearchHistoryDTO(
        input.id,
        input.keyword
    )

fun MyTokenDTO.toMyTokenEntity(): com.sohae.domain.myinformation.entity.MyTokenEntity =
    com.sohae.domain.myinformation.entity.MyTokenEntity(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )

fun com.sohae.domain.myinformation.entity.MyTokenEntity.toMyTokenDTO(): MyTokenDTO = MyTokenDTO(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken
)