package com.jhw.sohae.presentation.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhw.sohae.domain.myinformation.entity.MyAccountEntity
import com.jhw.sohae.domain.myinformation.entity.MyLeaveEntity
import com.jhw.sohae.domain.myinformation.entity.MyRankEntity
import com.jhw.sohae.domain.myinformation.entity.MyWelfareEntity
import com.jhw.sohae.domain.myinformation.entity.MyWorkInfoEntity
import com.jhw.sohae.domain.myinformation.usecase.MyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {

    private val _myAccount = MutableStateFlow<MyAccountEntity?>(null)
    val myAccount = _myAccount.asStateFlow()

    private val _myWorkInfo = MutableStateFlow<MyWorkInfoEntity?>(null)
    val myWorkInfo = _myWorkInfo.asStateFlow()

    private val _myWelfare = MutableStateFlow<MyWelfareEntity?>(null)
    val myWelfare = _myWelfare.asStateFlow()

    private val _myRank = MutableStateFlow<MyRankEntity?>(null)
    val myRank = _myRank.asStateFlow()

    private val _myLeave = MutableStateFlow<MyLeaveEntity?>(null)
    val myLeave = _myLeave.asStateFlow()

    val finishLoadDB = false
    val isReadyInfo = false

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateMyWorkInfo(idx: Int, value: Any) {
        val tmpMyWorkInfo = mutableListOf(
            myWorkInfo.value!!.workPlace,
            myWorkInfo.value!!.startWorkDay,
            myWorkInfo.value!!.finishWorkDay
        )

        tmpMyWorkInfo[idx] = value.toString().toLong()

        viewModelScope.launch {
            MyInfoUseCase.updateMyWorkInfo(
                input = MyWorkInfoEntity(
                    id = 0,
                    workPlace = tmpMyWorkInfo[0].toString(),
                    startWorkDay = tmpMyWorkInfo[1].toString().toLong(),
                    finishWorkDay = tmpMyWorkInfo[2].toString().toLong()
                ),
                updateRelatedInfo = (idx != 2)
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
            MyInfoUseCase.updateMyRank(
                MyRankEntity(
                    id = 0,
                    firstPromotionDay = tmpMyRank[0],
                    secondPromotionDay = tmpMyRank[1],
                    thirdPromotionDay = tmpMyRank[2]
                )
            )
        }
    }

    fun updateMyWelfare(idx: Int, value: Int) {
        val tmpMyWelfare = mutableListOf(
            myWelfare.value!!.lunchSupport,
            myWelfare.value!!.transportationSupport,
            myWelfare.value!!.payday
        )

        tmpMyWelfare[idx] = value

        viewModelScope.launch {
            MyInfoUseCase.updateMyWelfare(
                MyWelfareEntity(
                    id = 0,
                    lunchSupport = tmpMyWelfare[0],
                    transportationSupport = tmpMyWelfare[1],
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
            MyInfoUseCase.updateMyLeave(
                MyLeaveEntity(
                    id = 0,
                    firstAnnualLeave = tmpMyLeave[0],
                    secondAnnualLeave = tmpMyLeave[1],
                    sickLeave = tmpMyLeave[2]
                )
            )
        }
    }
}