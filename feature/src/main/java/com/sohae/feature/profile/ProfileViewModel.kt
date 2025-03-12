package com.sohae.feature.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohae.domain.myinformation.entity.MyLeaveEntity
import com.sohae.domain.myinformation.entity.MyRankEntity
import com.sohae.domain.myinformation.entity.MyWelfareEntity
import com.sohae.domain.myinformation.entity.MyWorkInfoEntity
import com.sohae.domain.myinformation.usecase.MyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val myInfoUseCase: com.sohae.domain.myinformation.usecase.MyInfoUseCase
): ViewModel() {

    val myAccount = myInfoUseCase.getMyAccount().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

    val myWorkInfo = myInfoUseCase.getMyWorkInfo().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

    val myWelfare = myInfoUseCase.getMyWelfare().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

    val myRank = myInfoUseCase.getMyRank().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

    val myLeave = myInfoUseCase.getMyLeave().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

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
            myInfoUseCase.updateMyWorkInfo(
                input = com.sohae.domain.myinformation.entity.MyWorkInfoEntity(
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
            myInfoUseCase.updateMyRank(
                com.sohae.domain.myinformation.entity.MyRankEntity(
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
            myInfoUseCase.updateMyWelfare(
                com.sohae.domain.myinformation.entity.MyWelfareEntity(
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
            myInfoUseCase.updateMyLeave(
                com.sohae.domain.myinformation.entity.MyLeaveEntity(
                    id = 0,
                    firstAnnualLeave = tmpMyLeave[0],
                    secondAnnualLeave = tmpMyLeave[1],
                    sickLeave = tmpMyLeave[2]
                )
            )
        }
    }
}