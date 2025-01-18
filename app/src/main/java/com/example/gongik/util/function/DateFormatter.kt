package com.example.gongik.util.function

import java.text.SimpleDateFormat
import java.util.Locale

fun getDate(value: Long): String =
    SimpleDateFormat("yyyy년 MM월 dd일", Locale("ko", "KR"))
        .format(value)

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