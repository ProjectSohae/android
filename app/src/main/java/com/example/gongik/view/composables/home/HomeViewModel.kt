package com.example.gongik.view.composables.home

import androidx.lifecycle.ViewModel
import com.example.gongik.R
import com.example.gongik.controller.MyInformationController

class HomeViewModel: ViewModel() {

    val myInformation = MyInformationController.myInformation
    val myWorkInformation = MyInformationController.myWorkInformation
    val myRank = MyInformationController.myRank
    val finishLoadDB = MyInformationController.finishLoadDB
    val isReadyInfo = MyInformationController.isReadyInfo
    val useVacationItemsList : List<Pair<String, Int>> = listOf(
        Pair("1년차 연차", R.drawable.outline_annual_leave_24),
        Pair("2년차 연차", R.drawable.outline_annual_leave_24),
        Pair("병가", R.drawable.outline_plus_bottle_24),
        Pair("기타 휴가", R.drawable.outline_annual_leave_24),
        Pair("외출", R.drawable.baseline_leave_early_24),
        Pair("조퇴", R.drawable.baseline_leave_early_24),
        Pair("복무이탈", R.drawable.baseline_warning_amber_24)
    )

    init {
    }
}