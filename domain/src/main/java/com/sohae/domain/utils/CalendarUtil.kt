package com.sohae.domain.utils

import java.time.DayOfWeek
import java.time.LocalDate

fun getWeekendCount(
    startDate: LocalDate,
    endDate: LocalDate
): Int {
    var currentDate = startDate
    var result = 0

    if (startDate.isAfter(endDate)) {
        return 0
    }

    while (true) {

        if (currentDate.isAfter(endDate)) { break }

        if (currentDate.dayOfWeek == DayOfWeek.SATURDAY
            || currentDate.dayOfWeek == DayOfWeek.SUNDAY) {
            result++
        }

        currentDate = currentDate.plusDays(1)
    }

    return result
}