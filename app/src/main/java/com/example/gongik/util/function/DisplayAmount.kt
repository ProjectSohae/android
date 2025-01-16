package com.example.gongik.util.function

fun displayAsAmount(input: String): String {
    var result = input
    var tmpLength = input.length - 3

    while (tmpLength > 0) {
        result = result.substring(IntRange(0, tmpLength - 1)) +
                "," +
                result.substring(IntRange(tmpLength, result.length - 1))
        tmpLength -= 3
    }

    return result
}