package com.example.gongik.controller

import com.example.gongik.model.data.myinformation.MyInformation
import com.example.gongik.model.data.myinformation.MyLeave
import com.example.gongik.model.data.myinformation.MyRank
import com.example.gongik.model.data.myinformation.MyUsedLeave
import com.example.gongik.model.data.myinformation.MyWelfare
import com.example.gongik.model.data.myinformation.MyWorkInformation
import com.example.gongik.model.repository.MyInformationDAO
import com.example.gongik.model.repository.MyInformationRepository
import com.example.gongik.model.repository.MyLeaveDAO
import com.example.gongik.model.repository.MyRankDAO
import com.example.gongik.model.repository.MyUsedLeaveDAO
import com.example.gongik.model.repository.MyWelfareDAO
import com.example.gongik.model.repository.MyWorkInformationDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object MyInformationController {

    private val myInfoDAO: MyInformationDAO = MyInformationRepository.myInformationDAO
    private val myWorkInfoDAO: MyWorkInformationDAO = MyInformationRepository.database.myWorkInformationDAO()
    private val myRankDAO: MyRankDAO = MyInformationRepository.database.myRankDAO()
    private val myWelfareDAO: MyWelfareDAO = MyInformationRepository.database.myWelfareDAO()
    private val myLeaveDAO: MyLeaveDAO = MyInformationRepository.database.myLeaveDAO()
    private val myUsedLeaveDAO: MyUsedLeaveDAO = MyInformationRepository.database.myUsedLeaveDAO()

    fun initMyInformation(
        callback: (MyInformation?) -> Unit
    ) {
        var myInfo: MyInformation?
        var myWorkInfo: MyWorkInformation? = null
        var myRank: MyRank? = null
        var myWelfare: MyWelfare? = null
        var myLeave: MyLeave? = null
        var myUsedLeaveList: List<MyUsedLeave>? = null

        CoroutineScope(Dispatchers.IO).launch {

            val myWorkInfoJob = CoroutineScope(Dispatchers.IO).launch {
                myWorkInfo = myWorkInfoDAO.selectAll()

                if (myWorkInfo == null) {
                    myWorkInfo = MyWorkInformation(
                        workPlace = "",
                        startWorkDay = -1,
                        finishWorkDay = -1
                    )

                    myWorkInfoDAO.insert(myWorkInfo!!)
                }
            }

            val myRankJob = CoroutineScope(Dispatchers.IO).launch {
                myRank = myRankDAO.selectAll()

                if (myRank == null) {
                    myRank = MyRank(
                        currentRank = -1,
                        firstPromotionDay = -1,
                        secondPromotionDay = -1,
                        thirdPromotionDay = -1
                    )

                    myRankDAO.insert(myRank!!)
                }
            }

            val myWelfareJob = CoroutineScope(Dispatchers.IO).launch {
                myWelfare = myWelfareDAO.selectAll()

                if (myWelfareDAO.selectAll() == null) {
                    myWelfare = MyWelfare(
                        foodCosts = -1,
                        transportationCosts = -1,
                        payday = -1
                    )

                    myWelfareDAO.insert(myWelfare!!)
                }
            }

            val myLeaveJob = CoroutineScope(Dispatchers.IO).launch {
                myLeave = myLeaveDAO.selectAll()

                if (myLeave == null) {
                    myLeave = MyLeave(
                        firstAnnualLeave = -1,
                        secondAnnualLeave = -1,
                        sickLeave = -1
                    )

                    myLeaveDAO.insert(myLeave!!)
                }
            }

            val myUsedLeaveListJob = CoroutineScope(Dispatchers.IO).launch {
                myUsedLeaveList = myUsedLeaveDAO.selectAll()

                if (myUsedLeaveList == null) {
                    myUsedLeaveList = emptyList()
                }
            }

            // 모든 db 작업이 끝날 때까지 대기
            runBlocking {
                myWorkInfoJob.join()
                myRankJob.join()
                myWelfareJob.join()
                myLeaveJob.join()
                myUsedLeaveListJob.join()
            }

            // 내 정보 불러오기 실패
            if (
                myWorkInfo == null
                || myRank == null
                || myWelfare == null
                || myLeave == null
                || myUsedLeaveList == null )
            {
                callback(null)
            }
            // 내 정보 불러오기 성공
            else {
                myInfo = myInfoDAO.selectAll()

                if (myInfo == null) {
                    myInfo = MyInformation(
                        realName = "",
                        nickname = "",
                        emailAddress = ""
                    )
                }

                myInfo!!.myWorkInformation = myWorkInfo!!
                myInfo!!.myRank = myRank!!
                myInfo!!.myWelfare = myWelfare!!
                myInfo!!.myLeave = myLeave!!
                myInfo!!.myUsedLeaveList = myUsedLeaveList!!

                myInfoDAO.insert(myInfo!!)

                callback(myInfo!!)
            }
        }
    }
}