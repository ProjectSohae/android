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

    private var _finishLoadDB = MutableStateFlow(false)
    val finishLoadDB = _finishLoadDB.asStateFlow()

    private var _isReadyInfo = MutableStateFlow(false)
    val isReadyInfo = _isReadyInfo.asStateFlow()

    val startPayDayList = mutableListOf<String>()
    val leaveDayList = mutableListOf<String>()

    init {
        for (idx in 1..27) {
            startPayDayList.add(idx.toString())
        }

        for (idx in 1..60){
            leaveDayList.add(idx.toString())
        }

        viewModelScope.launch {
            _myInformation.value = MyInformationController.initMyInformation()
            _myWorkInformation.value = MyInformationController.initMyWorkInformation()
            _myRank.value = MyInformationController.initMyRank()
            _myWelfare.value = MyInformationController.initMyWelfare()
            _myLeave.value = MyInformationController.initMyLeave()
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

    fun updateMyWorkInfo() {

    }

    fun updateMyRank() {

    }

    fun updateMyWelfare() {

    }

    fun updateMyLeave(idx: Int, days: Int) {
        val tmpMyLeave = mutableListOf(
            myLeave.value!!.firstAnnualLeave,
            myLeave.value!!.secondAnnualLeave,
            myLeave.value!!.sickLeave
        )

        tmpMyLeave[idx] = days

        viewModelScope.launch {
            MyInformationController.insertMyLeave(
                MyLeave(
                    firstAnnualLeave = tmpMyLeave[0],
                    secondAnnualLeave = tmpMyLeave[1],
                    sickLeave = tmpMyLeave[2]
                )
            )

            _myLeave.value = MyInformationController.initMyLeave()
        }
    }
}