package com.example.gongik.view.composables.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.gongik.R
import com.example.gongik.controller.MyInformationController
import com.example.gongik.controller.displayAsAmount
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

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

    fun getMyCurrentRank(
        currentTime: Long = System.currentTimeMillis()
    ): String {
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

        if (payday < 0
            || myWorkInformation.value!!.startWorkDay < 0) {
            return Pair("", "복무 대기 중")
        }

        if (myWorkInformation.value!!.finishWorkDay < System.currentTimeMillis()) { return Pair("", "소집 해제") }

        val currentDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(myWorkInformation.value!!.startWorkDay),
            ZoneId.systemDefault()
        ).plusMonths(monthsCount.toLong())
        val isLeapYear = (
                (currentDate.year % 4 == 0 && currentDate.year % 100 == 0)
                        || currentDate.year % 400 == 0
                )
        val getMonthDay: (Int) -> Int = { month ->
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
        val beforeRank = getMyCurrentRank(
            currentDate.minusMonths(1)
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        val currentRank = getMyCurrentRank()
        val beforeDayOfMonth = getMonthDay(currentDate.monthValue - 1)
        val currentDayOfMonth = getMonthDay(currentDate.monthValue)
        val beforeSalary = getMySalary(beforeRank) / beforeDayOfMonth
        val currentSalary = getMySalary(currentRank) / currentDayOfMonth

        val startWorkDay = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(myWorkInformation.value!!.startWorkDay),
            ZoneId.systemDefault()
        )
        val startDate = LocalDate.of(startWorkDay.year, startWorkDay.monthValue, payday)
            .plusMonths(monthsCount.toLong())
        val endDate = LocalDate.of(startWorkDay.year, startWorkDay.monthValue, payday)
            .plusMonths(monthsCount.toLong() + 1).minusDays(1)

        // 기본급 계산
        var resultDate = ""
        var resultSalary = 0
        if (monthsCount > 0) {
            resultDate = "${startDate.year}.${startDate.monthValue}.${startDate.dayOfMonth}" +
                    " - " +
                    "${endDate.year}.${endDate.monthValue}.${endDate.dayOfMonth}"
            resultSalary = ( beforeSalary * ( beforeDayOfMonth - payday + 1 ) ) + ( currentSalary * ( payday - 1 ) )
        } else {

            if (startWorkDay.dayOfMonth < payday) {
                resultDate = "${startWorkDay.year}.${startWorkDay.monthValue}.${startWorkDay.dayOfMonth}" +
                        " - " +
                        "${startDate.year}.${startDate.monthValue}.${startDate.dayOfMonth - 1}"
                resultSalary = currentSalary * (payday - startWorkDay.dayOfMonth)
            } else {
                resultDate = "${startWorkDay.year}.${startWorkDay.monthValue}.${startWorkDay.dayOfMonth}" +
                        " - " +
                        "${endDate.year}.${endDate.monthValue}.${endDate.dayOfMonth}"
                resultSalary = ( beforeSalary * ( beforeDayOfMonth - startWorkDay.dayOfMonth + 1 ) ) +
                        ( currentSalary * ( payday - 1 ) )
            }
        }

        // 주말, 휴가로 인한 식비 및 교통비 차감
        var noLunchSupportCount = 0
        var noTransportationSupportCount = 0


        return Pair(resultDate, displayAsAmount(resultSalary.toString()))
    }
}