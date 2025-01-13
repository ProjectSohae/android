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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.sql.Timestamp

object MyInformationController {

    private val myInfoDAO: MyInformationDAO = MyInformationRepository.myInformationDAO
    private val myWorkInfoDAO: MyWorkInformationDAO = MyInformationRepository.database.myWorkInformationDAO()
    private val myRankDAO: MyRankDAO = MyInformationRepository.database.myRankDAO()
    private val myWelfareDAO: MyWelfareDAO = MyInformationRepository.database.myWelfareDAO()
    private val myLeaveDAO: MyLeaveDAO = MyInformationRepository.database.myLeaveDAO()
    private val myUsedLeaveDAO: MyUsedLeaveDAO = MyInformationRepository.database.myUsedLeaveDAO()

    private var _myInformation = MutableStateFlow<MyInformation?>(null)
    val myInformation = _myInformation.asStateFlow()

    private var _myWorkInformation = MutableStateFlow<MyWorkInformation?>(null)
    val myWorkInformation = _myWorkInformation.asStateFlow()

    private var _myRank = MutableStateFlow<MyRank?>(null)
    val myRank = _myRank.asStateFlow()

    private var _myWelfare = MutableStateFlow<MyWelfare?>(null)
    val myWelfare = _myWelfare.asStateFlow()

    private var _myLeave = MutableStateFlow<MyLeave?>(null)
    val myLeave = _myLeave.asStateFlow()

    private var _myUsedLeaveList = MutableStateFlow<List<MyUsedLeave>>(emptyList())
    val myUsedLeave = _myUsedLeaveList.asStateFlow()

    private var _finishLoadDB = MutableStateFlow(false)
    val finishLoadDB = _finishLoadDB.asStateFlow()

    private var _isReadyInfo = MutableStateFlow(false)
    val isReadyInfo = _isReadyInfo.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            _myInformation.value = initMyInformation()
            _myWorkInformation.value = initMyWorkInformation()
            _myRank.value = initMyRank()
            _myWelfare.value = initMyWelfare()
            _myLeave.value = initMyLeave()
            _myUsedLeaveList.value = initMyUsedLeaveList()
            _finishLoadDB.value = true

            if (_myInformation.value != null
                && _myWorkInformation.value != null
                && _myRank.value != null
                && _myWelfare.value != null
                && _myLeave.value != null
            ) {
                _isReadyInfo.value = true
            }
        }
    }

    private suspend fun initMyInformation(): MyInformation? {
        var myInfo: MyInformation? = null

        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                myInfo = myInfoDAO.selectAll()

                if (myInfo == null) {
                    myInfo = MyInformation(
                        realName = "",
                        nickname = "",
                        emailAddress = ""
                    )

                    myInfoDAO.insert(myInfo!!)
                }
            }.join()
        }

        return myInfo
    }

    suspend fun updateMyInformation(inputMyInformation: MyInformation) {
        myInfoDAO.insert(inputMyInformation)
        _myInformation.value = initMyInformation()
    }

    private suspend fun initMyWorkInformation(): MyWorkInformation? {
        var myWorkInfo: MyWorkInformation? = null

        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                myWorkInfo = myWorkInfoDAO.selectAll()

                if (myWorkInfo == null) {
                    myWorkInfo = MyWorkInformation(
                        workPlace = "",
                        startWorkDay = -1,
                        finishWorkDay = -1
                    )

                    myWorkInfoDAO.insert(myWorkInfo!!)
                }
            }.join()
        }

        return myWorkInfo
    }

    suspend fun updateMyWorkInformation(inputMyWorkInformation: MyWorkInformation) {
        myWorkInfoDAO.insert(inputMyWorkInformation)
        _myWorkInformation.value = initMyWorkInformation()
    }

    private suspend fun initMyRank(): MyRank? {
        var myRank: MyRank? = null

        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
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
            }.join()
        }

        return myRank
    }

    suspend fun updateMyRank(inputMyRank: MyRank) {
        myRankDAO.insert(inputMyRank)
        _myRank.value = initMyRank()
    }

    private suspend fun initMyWelfare(): MyWelfare? {
        var myWelfare: MyWelfare? = null

        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                myWelfare = myWelfareDAO.selectAll()

                if (myWelfareDAO.selectAll() == null) {
                    myWelfare = MyWelfare(
                        foodCosts = -1,
                        transportationCosts = -1,
                        payday = -1
                    )

                    myWelfareDAO.insert(myWelfare!!)
                }
            }.join()
        }

        return myWelfare
    }

    suspend fun updateMyWelfare(inputMyWelfare: MyWelfare) {
        myWelfareDAO.insert(inputMyWelfare)
        _myWelfare.value = initMyWelfare()
    }

    private suspend fun initMyLeave(): MyLeave? {
        var myLeave: MyLeave? = null

        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                myLeave = myLeaveDAO.selectAll()

                if (myLeave == null) {
                    myLeave = MyLeave(
                        firstAnnualLeave = -1,
                        secondAnnualLeave = -1,
                        sickLeave = -1
                    )

                    myLeaveDAO.insert(myLeave!!)
                }
            }.join()
        }

        return myLeave
    }

    suspend fun updateMyLeave(inputMyLeave: MyLeave) {
        myLeaveDAO.insert(inputMyLeave)
        _myLeave.value = initMyLeave()
    }

    private suspend fun initMyUsedLeaveList(): List<MyUsedLeave> {
        var myUsedLeaveList: List<MyUsedLeave>? = null

        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                myUsedLeaveList = myUsedLeaveDAO.selectAll()

                if (myUsedLeaveList == null) {
                    myUsedLeaveList = emptyList()
                }
            }.join()
        }

        return myUsedLeaveList!!
    }
}