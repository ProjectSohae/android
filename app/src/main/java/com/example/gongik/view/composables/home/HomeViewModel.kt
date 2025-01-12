package com.example.gongik.view.composables.home

import androidx.compose.ui.unit.min
import androidx.lifecycle.ViewModel
import com.example.gongik.R
import com.example.gongik.controller.MyInformationController

class HomeViewModel: ViewModel() {

    val myInformation = MyInformationController.myInformation
    val myWorkInformation = MyInformationController.myWorkInformation
    val myRank = MyInformationController.myRank
    val myLeave = MyInformationController.myLeave
    val finishLoadDB = MyInformationController.finishLoadDB
    val isReadyInfo = MyInformationController.isReadyInfo
    val useVacationItemsList : List<Pair<String, Int>> = listOf(
        Pair("1년차 연차", R.drawable.outline_annual_leave_24),
        Pair("2년차 연차", R.drawable.outline_annual_leave_24),
        Pair("병가", R.drawable.outline_plus_bottle_24),
        Pair("기타 휴가", R.drawable.outline_annual_leave_24),
        Pair("복무이탈", R.drawable.baseline_warning_amber_24)
    )

    fun getRestLeaveTime(usedMinutes: Int, maxDays: Int): String {
        val maxMinutes = maxDays * 8 * 60
        var tmp = maxMinutes - usedMinutes
        var result: String = ""

        if (tmp <= 0) {
            return "0일"
        }

        val days: Int = tmp / (60 * 8)
        tmp %= 60 * 8

        val hours: Int = tmp / 60
        tmp %= 60

        val minutes: Int = tmp

        if (days > 0) {
            result += "${days}일"
        }

        if (hours > 0) {

            if (result.isNotBlank()) {
                result += " "
            }

            result += "${hours}시간"
        }

        if (minutes > 0) {

            if (result.isNotBlank()) {
                result += " "
            }

            result += "${minutes}분"
        }

        return result
    }

    fun getUsedLeaveTime(usedMinutes: Int): String {
        var tmp = usedMinutes
        var result: String = ""

        if (tmp <= 0) {
            return "0일"
        }

        val days: Int = tmp / (60 * 8)
        tmp %= 60 * 8

        val hours: Int = tmp / 60
        tmp %= 60

        val minutes: Int = tmp

        if (days > 0) {
            result += "${days}일"
        }

        if (hours > 0) {

            if (result.isNotBlank()) {
                result += " "
            }

            result += "${hours}시간"
        }

        if (minutes > 0) {

            if (result.isNotBlank()) {
                result += " "
            }

            result += "${minutes}분"
        }

        return result
    }
}