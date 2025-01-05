package com.example.gongik.view.composables.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gongik.controller.MyInformationController
import com.example.gongik.model.data.myinformation.MyInformation
import com.example.gongik.model.data.myinformation.MyLeave
import com.example.gongik.model.data.myinformation.MyRank
import com.example.gongik.model.data.myinformation.MyWelfare
import com.example.gongik.model.data.myinformation.MyWorkInformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

    fun updateMyWorkInfo() {

    }

    fun updateMyRank() {

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