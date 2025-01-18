package com.example.gongik.view.composables.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gongik.controller.MyInformationController
import com.example.gongik.model.data.myinformation.MyUsedLeave
import com.example.gongik.util.function.displayAsAmount
import com.example.gongik.util.function.getLeavePeriod
import com.example.gongik.util.function.getWeekendCount
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

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

    fun getTotalUsedLeaveTime(leaveKindIdx: Int): Long {
        var totalUsedLeaveTime: Long = 0

        runBlocking {
            val usedLeaveList = MyInformationController.getMyUsedListByLeaveKindIdx(leaveKindIdx)

            usedLeaveList.forEach { totalUsedLeaveTime += it.usedLeaveTime }
        }

        return totalUsedLeaveTime
    }

    fun getRestLeaveTime(leaveKindIdx: Int, maxDays: Int): String {
        return getLeavePeriod(
            (1000 * 60 * 60 * 8 * maxDays) -
                    getTotalUsedLeaveTime(leaveKindIdx)
        )
    }

    fun getMyCurrentRank(currentTime: Long): String {
        val promotionDays = listOf(
            myRank.value!!.firstPromotionDay,
            myRank.value!!.secondPromotionDay,
            myRank.value!!.thirdPromotionDay
        )
        val promotionDaysListSize = promotionDays.size
        var result = ranksList[0]

        if (promotionDays[0] < 0
            || currentTime < myWorkInformation.value!!.startWorkDay
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
            || System.currentTimeMillis() < myWorkInformation.value!!.startWorkDay
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

    // Pair < 기간, 월급 >
    @RequiresApi(Build.VERSION_CODES.O)
    fun getMyCurrentSalary(monthsCount: Int): Pair<String, String> {
        val payday: Int = myWelfare.value?.payday.let {
            if (it == null) { -1 } else {
                if (it > 0) { it } else { -1 }
            }
        }

        if (myWorkInformation.value!!.startWorkDay < 0
            || myWorkInformation.value!!.finishWorkDay < 0) {
            return Pair("", "복무 대기 중")
        }

        if (payday < 1) {
            return Pair("", "월급일 미설정")
        }

        if (myWorkInformation.value!!.finishWorkDay < System.currentTimeMillis()) {
            return Pair("", "소집 해제")
        }

        val startWorkDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(myWorkInformation.value!!.startWorkDay),
            ZoneId.systemDefault()
        ).toLocalDate()
        val currentDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(myWorkInformation.value!!.startWorkDay),
            ZoneId.systemDefault()
        ).plusMonths(monthsCount.toLong())
        val endWorkDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(myWorkInformation.value!!.finishWorkDay),
            ZoneId.systemDefault()
        ).toLocalDate()

        val getAllDayOfMonth: (Int, Int) -> Int = { year, month ->
            val isLeapYear = ((year % 4 == 0 && year % 100 == 0) || year % 400 == 0)

            allDayOfMonth[((month - 1) + 12) % 12] +
                    if (isLeapYear && currentDate.monthValue == 2) { 1 } else { 0 }
        }
        val getMySalary: (String) -> Int = { rank ->
            when (rank) {
                "이등병" -> { 750000 }
                "일등병" -> { 900000 }
                "상등병" -> { 1200000 }
                "병장" -> { 1500000 }
                else -> { 0 }
            }
        }
        val getWorkDate: (LocalDate, LocalDate) -> String = { startDate, endDate ->
            "${startDate.year}.${startDate.monthValue}.${startDate.dayOfMonth}" +
                    " - " +
                    "${endDate.year}.${endDate.monthValue}.${endDate.dayOfMonth}"
        }

        val startRank = getMyCurrentRank(
            currentDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        val endRank = getMyCurrentRank(
            currentDate.plusMonths(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        val allDayOfStartMonth = getAllDayOfMonth(currentDate.year, currentDate.monthValue)
        val allDayOfEndMonth = getAllDayOfMonth(
            currentDate.plusMonths(1).year, currentDate.plusMonths(1).monthValue
        )
        val startSalary = getMySalary(startRank) / allDayOfStartMonth
        val endSalary = getMySalary(endRank) / allDayOfEndMonth

        val beginPayDate = LocalDate.of(startWorkDate.year, startWorkDate.monthValue, payday)
            .plusMonths(monthsCount.toLong()).let {
                if (monthsCount > 0) { it } else { startWorkDate }
            }
        val endPayDate = LocalDate.of(beginPayDate.year, beginPayDate.monthValue, payday)
            .plusMonths(1).minusDays(1).let {
                if (monthsCount > 0) {
                    if (endWorkDate.isBefore(it)) { endWorkDate } else { it }
                } else {
                    if (beginPayDate.dayOfMonth < payday) {
                        LocalDate.of(beginPayDate.year, beginPayDate.monthValue, payday).minusDays(1)
                    } else {
                        if (endWorkDate.isBefore(it)) { endWorkDate } else { it }
                    }
                }
            }

        // 기본급 계산
        val weekendCount = getWeekendCount(beginPayDate, endPayDate)
        val totalWorkDay = ChronoUnit.DAYS.between(beginPayDate, endPayDate).toInt() + 1
        var resultDate = ""
        var resultSalary = 0
        resultDate = getWorkDate(beginPayDate, endPayDate)
        resultSalary = if (beginPayDate.monthValue == endPayDate.monthValue) {
            startSalary * (totalWorkDay)
        } else {
            startSalary * (beginPayDate.dayOfMonth - payday) + endSalary * (endPayDate.dayOfMonth)
        }

        // 주말, 휴가로 인한 식비 및 교통비 차감
        val noLunchSupportCount = 0
        val noTransportationSupportCount = 0
        resultSalary += myWelfare.value!!.lunchSupport * (totalWorkDay - weekendCount - noLunchSupportCount)
        resultSalary += myWelfare.value!!.transportationSupport * (totalWorkDay - weekendCount - noTransportationSupportCount)

        return Pair(resultDate, displayAsAmount(resultSalary.toString()))
    }

    fun takeMyLeave(inputMyUsedLeave: MyUsedLeave) {
        viewModelScope.launch {
            MyInformationController.updateMyUsedLeave(inputMyUsedLeave)
        }
    }
}