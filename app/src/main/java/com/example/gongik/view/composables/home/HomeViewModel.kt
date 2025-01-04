package com.example.gongik.view.composables.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gongik.controller.MyInformationController
import com.example.gongik.model.data.myinformation.MyInformation
import com.example.gongik.model.data.myinformation.MyLeave
import com.example.gongik.model.data.myinformation.MyRank
import com.example.gongik.model.data.myinformation.MyUsedLeave
import com.example.gongik.model.data.myinformation.MyWelfare
import com.example.gongik.model.data.myinformation.MyWorkInformation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

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
        viewModelScope.launch {
            _myInformation.value = MyInformationController.initMyInformation()
            _myWorkInformation.value = MyInformationController.initMyWorkInformation()
            _myRank.value = MyInformationController.initMyRank()
            _myWelfare.value = MyInformationController.initMyWelfare()
            _myLeave.value = MyInformationController.initMyLeave()
            _myUsedLeaveList.value = MyInformationController.initMyUsedLeaveList()
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
}