package com.sohae.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.Locale

fun getDate(value: Long): String =
    SimpleDateFormat("yyyy년 MM월 dd일", Locale("ko", "KR"))
        .format(value)

fun getDiffTimeFromNow(input: Instant): String {
    val targetTime = input.toLocalDateTime(TimeZone.currentSystemDefault())
    val nowTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val simpleFormat: (Int, Int, String) -> (String) = { target, now, timeDelimeter ->

        if (timeDelimeter == "년") {
            getDate(input.toEpochMilliseconds())
        } else {
            "${now - target}${timeDelimeter} 전"
        }
    }
    val timeList: List<Pair<Pair<Int, Int>, String>> = listOf(
        Pair(Pair(targetTime.year, nowTime.year), "년"),
        Pair(Pair(targetTime.monthNumber, nowTime.monthNumber), "달"),
        Pair(Pair(targetTime.dayOfMonth, nowTime.dayOfMonth), "일"),
        Pair(Pair(targetTime.hour, nowTime.hour), "시간"),
        Pair(Pair(targetTime.minute, nowTime.minute), "분"),
        Pair(Pair(targetTime.second, nowTime.second), "초")
    )
    var result: String = "방금 전"

    timeList.forEach forEach@{ item ->

        if (item.first.first < item.first.second) {
            result = simpleFormat(item.first.first, item.first.second, item.second)
            return@forEach
        }
    }

    return result
}

fun getLeavePeriod(value: Long): String {
    var tmp = value
    var result = ""

    if (value <= 0) { return "0일" }

    result += (tmp / (1000 * 60 * 60 * 8)).let {
        if (it > 0) { "${it}일" } else { "" }
    }
    tmp %= (1000 * 60 * 60 * 8)

    result += (tmp / (1000 * 60 * 60)).let {
        if (it > 0) {
            if (result.isNotBlank()) { " ${it}시간" } else { "${it}시간"  }
        } else { "" }
    }
    tmp %= (1000 * 60 * 60)

    result += (tmp / (1000 * 60)).let {
        if (it > 0) {
            if (result.isNotBlank()) { " ${it}분" } else { "${it}분"  }
        } else { "" }
    }

    return result
}