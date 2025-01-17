package com.example.gongik.util.function

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
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