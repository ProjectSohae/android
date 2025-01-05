package com.example.gongik.view.composables.home

import androidx.lifecycle.ViewModel
import com.example.gongik.controller.MyInformationController

class HomeViewModel: ViewModel() {

    val myInformation = MyInformationController.myInformation
    val myWorkInformation = MyInformationController.myWorkInformation
    val myRank = MyInformationController.myRank
    val finishLoadDB = MyInformationController.finishLoadDB
    val isReadyInfo = MyInformationController.isReadyInfo

    init {
    }
}