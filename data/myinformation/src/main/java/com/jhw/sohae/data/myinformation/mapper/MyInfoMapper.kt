package com.jhw.sohae.data.myinformation.mapper

import com.jhw.sohae.data.myinformation.dto.MyAccountDTO
import com.jhw.sohae.data.myinformation.dto.MyLeaveDTO
import com.jhw.sohae.data.myinformation.dto.MyRankDTO
import com.jhw.sohae.data.myinformation.dto.MySearchHistoryDTO
import com.jhw.sohae.data.myinformation.dto.MyUsedLeaveDTO
import com.jhw.sohae.data.myinformation.dto.MyWelfareDTO
import com.jhw.sohae.data.myinformation.dto.MyWorkInformationDTO
import com.jhw.sohae.domain.myinformation.entity.MyAccountEntity
import com.jhw.sohae.domain.myinformation.entity.MyLeaveEntity
import com.jhw.sohae.domain.myinformation.entity.MyRankEntity
import com.jhw.sohae.domain.myinformation.entity.MySearchHistoryEntity
import com.jhw.sohae.domain.myinformation.entity.MyUsedLeaveEntity
import com.jhw.sohae.domain.myinformation.entity.MyWelfareEntity
import com.jhw.sohae.domain.myinformation.entity.MyWorkInfoEntity

fun MyAccountMapper(input: MyAccountDTO): MyAccountEntity =
    MyAccountEntity(
        input.id,
        input.realName,
        input.nickname,
        input.emailAddress
    )

fun MyAccountMapper(input: MyAccountEntity): MyAccountDTO =
    MyAccountDTO(
        input.id,
        input.realName,
        input.nickname,
        input.emailAddress
    )

fun MyLeaveMapper(input: MyLeaveDTO): MyLeaveEntity =
    MyLeaveEntity(
        input.id,
        input.firstAnnualLeave,
        input.secondAnnualLeave,
        input.sickLeave
    )

fun MyLeaveMapper(input: MyLeaveEntity): MyLeaveDTO =
    MyLeaveDTO(
        input.id,
        input.firstAnnualLeave,
        input.secondAnnualLeave,
        input.sickLeave
    )

fun MyRankMapper(input: MyRankDTO): MyRankEntity =
    MyRankEntity(
        input.id,
        input.firstPromotionDay,
        input.secondPromotionDay,
        input.thirdPromotionDay
    )

fun MyRankMapper(input: MyRankEntity): MyRankDTO =
    MyRankDTO(
        input.id,
        input.firstPromotionDay,
        input.secondPromotionDay,
        input.thirdPromotionDay
    )

fun MyUsedLeaveMapper(input: MyUsedLeaveDTO): MyUsedLeaveEntity =
    MyUsedLeaveEntity(
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

fun MyUsedLeaveMapper(input: MyUsedLeaveEntity): MyUsedLeaveDTO =
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

fun MyWelfareMapper(input: MyWelfareDTO): MyWelfareEntity =
    MyWelfareEntity(
        input.id,
        input.lunchSupport,
        input.transportationSupport,
        input.payday
    )

fun MyWelfareMapper(input: MyWelfareEntity): MyWelfareDTO =
    MyWelfareDTO(
        input.id,
        input.lunchSupport,
        input.transportationSupport,
        input.payday
    )

fun MyWorkInfoMapper(input: MyWorkInformationDTO): MyWorkInfoEntity =
    MyWorkInfoEntity(
        input.id,
        input.workPlace,
        input.startWorkDay,
        input.finishWorkDay
    )

fun MyWorkInfoMapper(input: MyWorkInfoEntity): MyWorkInformationDTO =
    MyWorkInformationDTO(
        input.id,
        input.workPlace,
        input.startWorkDay,
        input.finishWorkDay
    )

fun MySearchHistoryMapper(input: MySearchHistoryDTO): MySearchHistoryEntity =
    MySearchHistoryEntity(
        input.id,
        input.keyword
    )

fun MySearchHistoryMapper(input: MySearchHistoryEntity): MySearchHistoryDTO =
    MySearchHistoryDTO(
        input.id,
        input.keyword
    )