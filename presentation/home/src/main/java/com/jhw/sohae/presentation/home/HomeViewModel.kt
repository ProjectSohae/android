package com.jhw.sohae.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhw.sohae.domain.myinformation.entity.MyUsedLeaveEntity
import com.jhw.sohae.domain.myinformation.usecase.MyInfoUseCase
import com.jhw.sohae.home.entity.SalaryDetailsEntity
import com.jhw.utils.getLeavePeriod
import com.jhw.utils.getWeekendCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel @Inject constructor(
    private val myInfoUseCase: MyInfoUseCase
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

    val leaveKindList: List<Pair<String, Int>> = listOf(
        Pair("1년차 연차", R.drawable.outline_annual_leave_24),
        Pair("2년차 연차", R.drawable.outline_annual_leave_24),
        Pair("병가", R.drawable.outline_plus_bottle_24),
        Pair("기타 휴가", R.drawable.outline_annual_leave_24),
        Pair("복무이탈", R.drawable.baseline_warning_amber_24)
    )

    val leaveTypeList = listOf(
        listOf( "연차", "오전 반차", "오후 반차", "지각", "조퇴", "외출" ),
        listOf( "연차", "오전 반차", "오후 반차", "지각", "조퇴", "외출" ),
        listOf( "연차", "오전 반차", "오후 반차", "지각", "조퇴", "외출" ),
        listOf( "특별 휴가", "청원 휴가", "공가", "대체 휴일" ),
        listOf( "무단 지각", "무단 조퇴", "무단 외출", "복무지 이탈(무단 결근)" )
    )

    private var _monthsCount = MutableStateFlow(0)
    val monthsCount = _monthsCount.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    private var _salaryDetails = MutableStateFlow(
        SalaryDetailsEntity(
            beginPayDate = LocalDate.MIN,
            endPayDate = LocalDate.MIN
        )
    )
    @RequiresApi(Build.VERSION_CODES.O)
    val salaryDetails = _salaryDetails.asStateFlow()

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
    private val rankSalary = mutableListOf(
        750000,
        900000,
        1200000,
        1500000
    )

    fun updateMonthsCount(input: Int) {
        if (input >= 0) {
            if (input > _monthsCount.value) {
                if (_salaryDetails.value.errorMessage != "소집 해제") {
                    _monthsCount.value = input
                }
            } else {
                _monthsCount.value = input
            }
        } else {
            _monthsCount.value = 0
        }
    }

    fun updateSalaryDetails() {
        _salaryDetails.value = getMyCurrentSalary(monthsCount.value ?: 0)
    }

    fun getTotalUsedLeaveTime(leaveKindIdx: Int): Long {
        var totalUsedLeaveTime: Long = 0

        runBlocking {
            val usedLeaveList = myInfoUseCase.getMyUsedLeaveListByLeaveKindIdx(leaveKindIdx)

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
            || currentTime < myWorkInfo.value!!.startWorkDay
            || myWorkInfo.value!!.startWorkDay < 0) {
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
            myWorkInfo.value!!.startWorkDay < 0
            || System.currentTimeMillis() < myWorkInfo.value!!.startWorkDay
            || myWorkInfo.value!!.finishWorkDay < System.currentTimeMillis()
            || myWorkInfo.value!!.finishWorkDay < myWorkInfo.value!!.startWorkDay)
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
    fun getSlackOffDays(startDate: LocalDate, endDate: LocalDate): Int {
        lateinit var slackOffList: List<MyUsedLeaveEntity>

        runBlocking {
            slackOffList = myInfoUseCase.getMyUsedLeaveListByDate(
                startDate = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                endDate = endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            ).filter { it.leaveKindIdx == 4 && it.leaveTypeIdx == 3 }
        }

        return slackOffList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNotReceiveLunchSupportDays(startDate: LocalDate, endDate: LocalDate): Int {
        var result = 0
        var isExistLeave = false
        var currentDateValue = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endDateValue = endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        var leaveList: List<MyUsedLeaveEntity> = emptyList()

        runBlocking {
            leaveList = myInfoUseCase.getMyUsedLeaveListByDate(
                startDate = currentDateValue,
                endDate = endDateValue
            ).filter { !it.receiveLunchSupport }
        }

        while (currentDateValue <= endDateValue) {

            val currentDayOfWeek = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentDateValue), ZoneId.systemDefault()).dayOfWeek

            if (currentDayOfWeek != DayOfWeek.SATURDAY && currentDayOfWeek != DayOfWeek.SUNDAY) {
                leaveList.forEach breaker@{
                    if (it.leaveEndDate > it.leaveStartDate) {
                        if (currentDateValue in it.leaveStartDate..it.leaveEndDate) {
                            isExistLeave = true
                            return@breaker
                        }
                    } else {
                        if (currentDateValue == it.leaveStartDate) {
                            isExistLeave = true
                            return@breaker
                        }
                    }
                }

                if (isExistLeave) { result++ }
            }

            isExistLeave = false
            currentDateValue += 1000 * 60 * 60 * 24
        }

        return result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNotReceiveTransportationSupportDays(startDate: LocalDate, endDate: LocalDate): Int {
        var result = 0
        var isExistLeave = false
        var currentDateValue = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endDateValue = endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        var leaveList: List<MyUsedLeaveEntity> = emptyList()

        runBlocking {
            leaveList = myInfoUseCase.getMyUsedLeaveListByDate(
                startDate = currentDateValue,
                endDate = endDateValue
            ).filter { !it.receiveTransportationSupport }
        }

        while (currentDateValue <= endDateValue) {

            val currentDayOfWeek = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentDateValue), ZoneId.systemDefault()).dayOfWeek

            if (currentDayOfWeek != DayOfWeek.SATURDAY && currentDayOfWeek != DayOfWeek.SUNDAY) {
                leaveList.forEach breaker@{
                    if (it.leaveEndDate > it.leaveStartDate) {
                        if (currentDateValue in it.leaveStartDate..it.leaveEndDate) {
                            isExistLeave = true
                            return@breaker
                        }
                    } else {
                        if (currentDateValue == it.leaveStartDate) {
                            isExistLeave = true
                            return@breaker
                        }
                    }
                }

                if (isExistLeave) { result++ }
            }

            isExistLeave = false
            currentDateValue += 1000 * 60 * 60 * 24
        }

        return result
    }

    // Pair < 기간, 월급 >
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMyCurrentSalary(monthsCount: Int): SalaryDetailsEntity {
        val payday: Int = myWelfare.value?.payday.let {
            if (it == null) { -1 } else {
                if (it > 0) { it } else { -1 }
            }
        }

        if (myWorkInfo.value!!.startWorkDay < 0
            || myWorkInfo.value!!.finishWorkDay < 0) {
            return SalaryDetailsEntity(
                beginPayDate = LocalDate.MIN,
                endPayDate = LocalDate.MIN,
                errorMessage = "복무 대기 중"
            )
        }

        if (payday < 1) {
            return SalaryDetailsEntity(
                beginPayDate = LocalDate.MIN,
                endPayDate = LocalDate.MIN,
                errorMessage = "월급일 미설정"
            )
        }

        val startWorkDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(myWorkInfo.value!!.startWorkDay),
            ZoneId.systemDefault()
        ).toLocalDate()
        val currentDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(myWorkInfo.value!!.startWorkDay),
            ZoneId.systemDefault()
        ).plusMonths(monthsCount.toLong())
        val endWorkDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(myWorkInfo.value!!.finishWorkDay),
            ZoneId.systemDefault()
        ).toLocalDate()

        val getAllDayOfMonth: (Int, Int) -> Int = { year, month ->
            val isLeapYear = ((year % 4 == 0 && year % 100 == 0) || year % 400 == 0)

            allDayOfMonth[((month - 1) + 12) % 12] +
                    if (isLeapYear && currentDate.monthValue == 2) { 1 } else { 0 }
        }
        val getMySalary: (String) -> Int = { rank ->
            when (rank) {
                "이등병" -> { rankSalary[0] }
                "일등병" -> { rankSalary[1] }
                "상등병" -> { rankSalary[2] }
                "병장" -> { rankSalary[3] }
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
                if (endWorkDate.isBefore(it)) { endWorkDate } else { it }
            }

        if (endPayDate.isBefore(beginPayDate)) {
            return SalaryDetailsEntity(
                beginPayDate = LocalDate.MIN,
                endPayDate = LocalDate.MIN,
                errorMessage = "소집 해제"
            )
        }

        // 기본급 계산
        val slackOffCountInStartMonth = getSlackOffDays(
            startDate = beginPayDate,
            endDate = if (beginPayDate.monthValue == endPayDate.monthValue) {
                endPayDate
            } else {
                LocalDate.of(beginPayDate.year, beginPayDate.monthValue, allDayOfStartMonth)
            }
        )
        val slackOffCountInEndMonth = getSlackOffDays(
            startDate = LocalDate.of(endPayDate.year, endPayDate.monthValue, 1),
            endDate = endPayDate
        )
        val weekendCount = getWeekendCount(beginPayDate, endPayDate)
        val totalWorkDay = ChronoUnit.DAYS.between(beginPayDate, endPayDate).toInt() + 1
        val resultDate = getWorkDate(beginPayDate, endPayDate)
        var resultSalary = if (beginPayDate.monthValue == endPayDate.monthValue) {
            startSalary * (totalWorkDay - slackOffCountInStartMonth)
        } else {
            startSalary * (allDayOfStartMonth - beginPayDate.dayOfMonth + 1 - slackOffCountInStartMonth) +
                    endSalary * (endPayDate.dayOfMonth - slackOffCountInEndMonth)
        }

        // 주말, 휴가로 인한 식비 및 교통비 차감
        val noLunchSupportCountInStartMonth = getNotReceiveLunchSupportDays(
            startDate = beginPayDate,
            endDate = if (beginPayDate.monthValue == endPayDate.monthValue) {
                endPayDate
            } else {
                LocalDate.of(beginPayDate.year, beginPayDate.monthValue, allDayOfStartMonth)
            }
        )
        val noLunchSupportCountInEndMonth = if (beginPayDate.monthValue == endPayDate.monthValue) {
            0
        } else {
            getNotReceiveLunchSupportDays(
                startDate = LocalDate.of(endPayDate.year, endPayDate.monthValue, 1),
                endDate = endPayDate
            )
        }
        val noTransportationSupportCountInStartMonth = getNotReceiveTransportationSupportDays(
            startDate = beginPayDate,
            endDate = if (beginPayDate.monthValue == endPayDate.monthValue) {
                endPayDate
            } else {
                LocalDate.of(beginPayDate.year, beginPayDate.monthValue, allDayOfStartMonth)
            }
        )
        val noTransportationSupportCountInEndMonth = if (beginPayDate.monthValue == endPayDate.monthValue) {
            0
        } else {
            getNotReceiveTransportationSupportDays(
                startDate = LocalDate.of(endPayDate.year, endPayDate.monthValue, 1),
                endDate = endPayDate
            )
        }
        resultSalary += myWelfare.value!!.lunchSupport *
                (totalWorkDay
                        - (weekendCount
                        + (noLunchSupportCountInStartMonth + noLunchSupportCountInEndMonth))
                        )
        resultSalary += myWelfare.value!!.transportationSupport *
                (totalWorkDay
                        - (weekendCount
                        + (noTransportationSupportCountInStartMonth + noTransportationSupportCountInEndMonth))
                        )

        return SalaryDetailsEntity(
            startRank = startRank,
            endRank = endRank,
            allDayOfStartMonth = allDayOfStartMonth,
            allDayOfEndMonth = allDayOfEndMonth,
            startSalary = startSalary,
            startSalaryPerMonth = getMySalary(startRank),
            endSalary = endSalary,
            endSalaryPerMonth = getMySalary(endRank),
            slackOffCountInStartMonth = slackOffCountInStartMonth,
            slackOffCountInEndMonth = slackOffCountInEndMonth,
            lunchSupport = myWelfare.value!!.lunchSupport,
            noLunchSupportCountInStartMonth = noLunchSupportCountInStartMonth,
            noLunchSupportCountInEndMonth = noLunchSupportCountInEndMonth,
            transportationSupport = myWelfare.value!!.transportationSupport,
            noTransportationSupportCountInStartMonth = noTransportationSupportCountInStartMonth,
            noTransportationSupportCountInEndMonth = noTransportationSupportCountInEndMonth,
            beginPayDate = beginPayDate,
            endPayDate = endPayDate,
            totalWorkDay = totalWorkDay,
            resultDate = resultDate,
            resultSalary = resultSalary
        )
    }

    fun takeMyLeave(inputMyUsedLeave: MyUsedLeaveEntity) {
        viewModelScope.launch {
            myInfoUseCase.updateMyUsedLeave(inputMyUsedLeave)
        }
    }

    fun getMyUsedLeaveListByLeaveKindIdx(leaveKindIdx: Int): List<MyUsedLeaveEntity> {
        return runBlocking {
             return@runBlocking myInfoUseCase.getMyUsedLeaveListByLeaveKindIdx(leaveKindIdx)
        }
    }

    fun getMyUsedLeaveListByDate(startDateValue: Long, endDateValue: Long): List<MyUsedLeaveEntity> {
        return runBlocking {
            return@runBlocking myInfoUseCase.getMyUsedLeaveListByDate(startDateValue, endDateValue)
        }
    }

    fun deleteMyUsedLeave(deleteLeaveItemId: Int) {
        runBlocking {
            myInfoUseCase.deleteMyUsedLeave(deleteLeaveItemId)
        }
    }
}