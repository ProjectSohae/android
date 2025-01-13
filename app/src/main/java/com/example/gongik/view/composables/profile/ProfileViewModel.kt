package com.example.gongik.view.composables.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gongik.controller.MyInformationController
import com.example.gongik.model.data.myinformation.MyLeave
import com.example.gongik.model.data.myinformation.MyRank
import com.example.gongik.model.data.myinformation.MyWelfare
import com.example.gongik.model.data.myinformation.MyWorkInformation
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {

    val myInformation = MyInformationController.myInformation
    val myWorkInformation = MyInformationController.myWorkInformation
    val myRank = MyInformationController.myRank
    val myWelfare = MyInformationController.myWelfare
    val myLeave = MyInformationController.myLeave
    val finishLoadDB = MyInformationController.finishLoadDB
    val isReadyInfo = MyInformationController.isReadyInfo

    val startPayDayList = mutableListOf<String>()
    val leaveDayList = mutableListOf<String>()

    init {
        for (idx in 1..27) {
            startPayDayList.add(idx.toString())
        }

        for (idx in 1..60){
            leaveDayList.add(idx.toString())
        }
    }

    fun updateMyWorkInfo(idx: Int, value: Any) {
        val tmpMyWorkInfo = mutableListOf(
            myWorkInformation.value!!.workPlace,
            myWorkInformation.value!!.startWorkDay,
            myWorkInformation.value!!.finishWorkDay
        )

        tmpMyWorkInfo[idx] = value.toString().toLong()

        viewModelScope.launch {
            MyInformationController.updateMyWorkInformation(
                MyWorkInformation(
                    workPlace = tmpMyWorkInfo[0].toString(),
                    startWorkDay = tmpMyWorkInfo[1].toString().toLong(),
                    finishWorkDay = tmpMyWorkInfo[2].toString().toLong()
                )
            )
        }
    }

    fun updateMyRank(idx: Int, value: Long) {
        val tmpMyRank = mutableListOf(
            myRank.value!!.firstPromotionDay,
            myRank.value!!.secondPromotionDay,
            myRank.value!!.thirdPromotionDay
        )

        tmpMyRank[idx] = value

        viewModelScope.launch {
            MyInformationController.updateMyRank(
                MyRank(
                    firstPromotionDay = tmpMyRank[0],
                    secondPromotionDay = tmpMyRank[1],
                    thirdPromotionDay = tmpMyRank[2]
                )
            )
        }
    }

    fun updateMyWelfare(idx: Int, value: Int) {
        val tmpMyWelfare = mutableListOf(
            myWelfare.value!!.foodCosts,
            myWelfare.value!!.transportationCosts,
            myWelfare.value!!.payday
        )

        tmpMyWelfare[idx] = value

        viewModelScope.launch {
            MyInformationController.updateMyWelfare(
                MyWelfare(
                    foodCosts = tmpMyWelfare[0],
                    transportationCosts = tmpMyWelfare[1],
                    payday = tmpMyWelfare[2]
                )
            )
        }
    }

    fun updateMyLeave(idx: Int, days: Int) {
        val tmpMyLeave = mutableListOf(
            myLeave.value!!.firstAnnualLeave,
            myLeave.value!!.secondAnnualLeave,
            myLeave.value!!.sickLeave
        )

        tmpMyLeave[idx] = days

        viewModelScope.launch {
            MyInformationController.updateMyLeave(
                MyLeave(
                    firstAnnualLeave = tmpMyLeave[0],
                    secondAnnualLeave = tmpMyLeave[1],
                    sickLeave = tmpMyLeave[2]
                )
            )
        }
    }
}