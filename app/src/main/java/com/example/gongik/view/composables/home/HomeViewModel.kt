package com.example.gongik.view.composables.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.gongik.R
import com.example.gongik.controller.MyInformationController
import com.example.gongik.controller.displayAsAmount
import java.time.LocalDateTime

class HomeViewModel: ViewModel() {

    val myInformation = MyInformationController.myInformation
    val myWorkInformation = MyInformationController.myWorkInformation
    val myWelfare = MyInformationController.myWelfare
    val myRank = MyInformationController.myRank
    val myLeave = MyInformationController.myLeave
    val finishLoadDB = MyInformationController.finishLoadDB
    val isReadyInfo = MyInformationController.isReadyInfo

    private val allDayOfMonth = listOf(
        31, 28, 31, 30,
        31, 30, 31, 31,
        30, 31, 30, 31
    )
    private val ranksList = listOf(
        "이등병",
        "일등병",
        "상등병",
        "병장"
    )
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
        var result = ""

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
        var result = ""

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

    fun getMyCurrentRank(): String {
        val currentTime = System.currentTimeMillis()
        val promotionDays = listOf(
            myRank.value!!.firstPromotionDay,
            myRank.value!!.secondPromotionDay,
            myRank.value!!.thirdPromotionDay
        )
        val promotionDaysListSize = promotionDays.size
        var result = ranksList[0]

        if (promotionDays[0] < 0
            || myWorkInformation.value!!.startWorkDay < 0) {
            return "보수 등급 없음"
        }

        for (idx: Int in 0..<promotionDaysListSize) {

            if (promotionDays[idx] in 0..currentTime) {
                result = ranksList[idx + 1]
            } else { break }
        }

        return result
    }

    fun getNextPromotionDay(): String {
        val currentTime = System.currentTimeMillis()
        val promotionDays = listOf(
            myRank.value!!.firstPromotionDay,
            myRank.value!!.secondPromotionDay,
            myRank.value!!.thirdPromotionDay
        )
        val promotionDaysListSize = promotionDays.size
        var result: Long = -1

        if (
            myWorkInformation.value!!.startWorkDay < 0
            || myWorkInformation.value!!.finishWorkDay < System.currentTimeMillis()
            || myWorkInformation.value!!.finishWorkDay < myWorkInformation.value!!.startWorkDay)
        {
            return "해당 없음"
        }

        for (idx: Int in 0..<promotionDaysListSize) {

            if (promotionDays[idx] in 0..currentTime) {
                continue
            } else {

                if (promotionDays[idx] >= 0) {
                    result = promotionDays[idx] - currentTime
                }

                break
            }
        }

        return result.let {
            if (it < 0) {
                "해당 없음"
            } else {
                "${(it / (1000 * 60 * 60 * 24)) + 1}일 남음"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMyCurrentSalary(): String {
        val isLeapYear = (
                (LocalDateTime.now().year % 4 == 0 && LocalDateTime.now().year % 100 == 0)
                        || LocalDateTime.now().year % 400 == 0
                )
        val currentDayOfMonth = allDayOfMonth[LocalDateTime.now().monthValue - 1] +
                if (isLeapYear && LocalDateTime.now().monthValue == 2) { 1 } else { 0 }
        var noLunchCostCount = 0
        var noTransportationCostCount = 0
        val currentRank = getMyCurrentRank()
        var currentSalary = 0

        when (currentRank) {
            "이등병" -> {
                currentSalary = 750000
            }
            "일등병" -> {
                currentSalary = 900000
            }
            "상등병" -> {
                currentSalary = 1200000
            }
            "병장" -> {
                currentSalary = 1500000
            }
            else -> {
                currentSalary = 0
            }
        }

        val workDayRatio = currentDayOfMonth.toFloat() / currentDayOfMonth.toFloat()
        currentSalary = (currentSalary * workDayRatio).toInt()

        currentSalary += myWelfare.value!!.lunchSupport.let {
            if (it > 0) { it * (currentDayOfMonth - noLunchCostCount) } else { 0 }
        }

        currentSalary += myWelfare.value!!.transportationSupport.let {
            if (it > 0) { it * (currentDayOfMonth - noTransportationCostCount) } else { 0 }
        }

        return displayAsAmount(currentSalary.toString())
    }
}