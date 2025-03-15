package com.sohae.domain.utils

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
    val timeList: List<Pair<Long, String>> = listOf(
        Pair(1000, "초"),
        Pair(60, "분"),
        Pair(60, "시간"),
        Pair(24, "일"),
        Pair(30, "달"),
        Pair(365, "년")
    )
    var diffTime = Clock.System.now().toEpochMilliseconds() - input.toEpochMilliseconds()
    var result = "방금 전"

    timeList.forEach forEach@{ item ->

        if (diffTime / item.first > 0) {
            diffTime /= item.first
            result = "${diffTime}${item.second} 전"
        } else {
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