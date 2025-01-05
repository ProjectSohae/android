package com.example.gongik.controller

fun Int.displayAsAmount(): String {
    var idx = 0
    var tmp = this
    var result = ""

    if (tmp > 0) {

        while (tmp > 0) {

            if (idx > 0 && idx % 3 == 0) {
                result += ','
            }

            result += ((tmp % 10) + '0'.code).toChar()

            tmp /= 10
            idx++
        }
    } else if (tmp == 0) {
        result += '0'
    }

    return result.reversed()
}