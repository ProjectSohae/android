package com.example.gongik.view.composables

import java.text.SimpleDateFormat
import java.util.Locale

fun getDate(value: Long): String =
    SimpleDateFormat("yyyy년 MM월 dd일", Locale("ko", "KR"))
        .format(value)