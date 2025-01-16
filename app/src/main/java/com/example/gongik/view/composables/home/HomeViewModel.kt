package com.example.gongik.view.composables.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.gongik.R
import com.example.gongik.controller.MyInformationController
import com.example.gongik.util.function.displayAsAmount
import com.example.gongik.util.function.getWeekendCount
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

        if (payday < 1
            || myWorkInformation.value!!.startWorkDay < 0) {
            return Pair("", "복무 대기 중")
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

        val isLeapYear = (
                (currentDate.year % 4 == 0 && currentDate.year % 100 == 0)
                        || currentDate.year % 400 == 0
                )
        val getAllDayOfMonth: (Int) -> Int = { month ->
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

        val beforeRank = getMyCurrentRank(
            currentDate.minusMonths(1)
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        val currentRank = getMyCurrentRank(
            currentDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        val beforeAllDayOfMonth = getAllDayOfMonth(currentDate.monthValue - 1)
        val currentAllDayOfMonth = getAllDayOfMonth(currentDate.monthValue)
        val beforeSalary = getMySalary(beforeRank) / beforeAllDayOfMonth
        val currentSalary = getMySalary(currentRank) / currentAllDayOfMonth

        val beginDate = LocalDate.of(startWorkDate.year, startWorkDate.monthValue, payday)
            .plusMonths(monthsCount.toLong())
        val endDate = LocalDate.of(startWorkDate.year, startWorkDate.monthValue, payday)
            .plusMonths(monthsCount.toLong() + 1).minusDays(1)

        // 기본급 계산
        val weekendCount: Int
        val totalWorkDay: Int
        var resultDate = ""
        var resultSalary = 0
        if (monthsCount > 0) {
            resultDate = getWorkDate(beginDate, endDate)
            resultSalary = ( beforeSalary * ( beforeAllDayOfMonth - payday + 1 ) ) + ( currentSalary * ( payday - 1 ) )
            totalWorkDay = beforeAllDayOfMonth
            weekendCount = getWeekendCount(beginDate, endDate)
        } else {

            if (startWorkDate.dayOfMonth < payday) {
                resultDate = getWorkDate(startWorkDate, beginDate.minusDays(1))
                resultSalary = currentSalary * (payday - startWorkDate.dayOfMonth)
                totalWorkDay = payday - startWorkDate.dayOfMonth
                weekendCount = getWeekendCount(startWorkDate, beginDate)
            } else {
                resultDate = getWorkDate(startWorkDate, endDate)
                resultSalary = ( beforeSalary * ( beforeAllDayOfMonth - startWorkDate.dayOfMonth + 1 ) ) +
                        ( currentSalary * ( payday - 1 ) )
                totalWorkDay = (beforeAllDayOfMonth - startWorkDate.dayOfMonth + 1) + (payday - 1)
                weekendCount = getWeekendCount(startWorkDate, endDate)
            }
        }

        // 주말, 휴가로 인한 식비 및 교통비 차감
        val noLunchSupportCount = 0
        val noTransportationSupportCount = 0
        resultSalary += myWelfare.value!!.lunchSupport * (totalWorkDay - weekendCount - noLunchSupportCount)
        resultSalary += myWelfare.value!!.transportationSupport * (totalWorkDay - weekendCount - noTransportationSupportCount)

        return Pair(resultDate, displayAsAmount(resultSalary.toString()))
    }
}