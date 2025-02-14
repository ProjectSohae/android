package com.jhw.sohae.data.repositoryimpl.myinformation

import com.jhw.sohae.data.model.myinformation.MyAccountDTO
import com.jhw.sohae.data.model.myinformation.MyLeaveDTO
import com.jhw.sohae.data.model.myinformation.MyRankDTO
import com.jhw.sohae.data.model.myinformation.MyUsedLeaveDTO
import com.jhw.sohae.data.model.myinformation.MyWelfareDTO
import com.jhw.sohae.data.model.myinformation.MyWorkInformationDTO
import com.jhw.sohae.domain.myinformation.entity.MyAccountEntity
import com.jhw.sohae.domain.myinformation.entity.MyLeaveEntity
import com.jhw.sohae.domain.myinformation.entity.MyRankEntity
import com.jhw.sohae.domain.myinformation.entity.MyUsedLeaveEntity
import com.jhw.sohae.domain.myinformation.entity.MyWelfareEntity
import com.jhw.sohae.domain.myinformation.entity.MyWorkInfoEntity

fun MyInfoMapper(myInfoDTO: MyAccountDTO): MyAccountEntity =
    MyAccountEntity(
        myInfoDTO.id,
        myInfoDTO.realName,
        myInfoDTO.nickname,
        myInfoDTO.emailAddress
    )

fun MyInfoMapper(myAccountEntity: MyAccountEntity): MyAccountDTO = MyAccountDTO(
    myAccountEntity.id,
    myAccountEntity.realName,
    myAccountEntity.nickname,
    myAccountEntity.emailAddress
)

fun MyLeaveMapper(myLeaveDTO: MyLeaveDTO): MyLeaveEntity =
    MyLeaveEntity(
        myLeaveDTO.id,
        myLeaveDTO.firstAnnualLeave,
        myLeaveDTO.secondAnnualLeave,
        myLeaveDTO.sickLeave
    )

fun MyLeaveMapper(myLeaveEntity: MyLeaveEntity): MyLeaveDTO = MyLeaveDTO(
    myLeaveEntity.id,
    myLeaveEntity.firstAnnualLeave,
    myLeaveEntity.secondAnnualLeave,
    myLeaveEntity.sickLeave
)

fun MyRankMapper(myRankDTO: MyRankDTO): MyRankEntity =
    MyRankEntity(
        myRankDTO.id,
        myRankDTO.firstPromotionDay,
        myRankDTO.secondPromotionDay,
        myRankDTO.thirdPromotionDay
    )

fun MyRankMapper(myRankEntity: MyRankEntity): MyRankDTO = MyRankDTO(
    myRankEntity.id,
    myRankEntity.firstPromotionDay,
    myRankEntity.secondPromotionDay,
    myRankEntity.thirdPromotionDay
)

fun MyUsedLeaveMapper(myUsedLeaveDTO: MyUsedLeaveDTO): MyUsedLeaveEntity =
    MyUsedLeaveEntity(
        myUsedLeaveDTO.id,
        myUsedLeaveDTO.leaveKindIdx,
        myUsedLeaveDTO.leaveTypeIdx,
        myUsedLeaveDTO.reason,
        myUsedLeaveDTO.usedLeaveTime,
        myUsedLeaveDTO.leaveStartDate,
        myUsedLeaveDTO.leaveEndDate,
        myUsedLeaveDTO.receiveLunchSupport,
        myUsedLeaveDTO.receiveTransportationSupport
    )

fun MyUsedLeaveMapper(myUsedLeaveEntity: MyUsedLeaveEntity): MyUsedLeaveDTO = MyUsedLeaveDTO(
    myUsedLeaveEntity.id,
    myUsedLeaveEntity.leaveKindIdx,
    myUsedLeaveEntity.leaveTypeIdx,
    myUsedLeaveEntity.reason,
    myUsedLeaveEntity.usedLeaveTime,
    myUsedLeaveEntity.leaveStartDate,
    myUsedLeaveEntity.leaveEndDate,
    myUsedLeaveEntity.receiveLunchSupport,
    myUsedLeaveEntity.receiveTransportationSupport
)

fun MyWelfareMapper(myWelfareDTO: MyWelfareDTO): MyWelfareEntity =
    MyWelfareEntity(
        myWelfareDTO.id,
        myWelfareDTO.lunchSupport,
        myWelfareDTO.transportationSupport,
        myWelfareDTO.payday
    )

fun MyWelfareMapper(myWelfareEntity: MyWelfareEntity): MyWelfareDTO = MyWelfareDTO(
    myWelfareEntity.id,
    myWelfareEntity.lunchSupport,
    myWelfareEntity.transportationSupport,
    myWelfareEntity.payday
)

fun MyWorkInfoMapper(myWorkInformationDTO: MyWorkInformationDTO): MyWorkInfoEntity =
    MyWorkInfoEntity(
        myWorkInformationDTO.id,
        myWorkInformationDTO.workPlace,
        myWorkInformationDTO.startWorkDay,
        myWorkInformationDTO.finishWorkDay
    )

fun MyWorkInfoMapper(myWorkInfoEntity: MyWorkInfoEntity): MyWorkInformationDTO = MyWorkInformationDTO(
    myWorkInfoEntity.id,
    myWorkInfoEntity.workPlace,
    myWorkInfoEntity.startWorkDay,
    myWorkInfoEntity.finishWorkDay
)