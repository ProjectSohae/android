package com.example.gongik.model.data

import java.time.LocalDate

data class SalaryDetails(
    val startRank: String = "",
    val endRank: String = "",
    val allDayOfStartMonth: Int = 0,
    val allDayOfEndMonth: Int = 0,
    val startSalary: Int = 0,
    val startSalaryPerMonth: Int = 0,
    val endSalary: Int = 0,
    val endSalaryPerMonth: Int = 0,
    val lunchSupport: Int = 0,
    val transportationSupport: Int = 0,
    val beginPayDate: LocalDate,
    val endPayDate: LocalDate,
    val weekendCount: Int = 0,
    val totalWorkDay: Int = 0,
    val resultDate: String = "",
    val resultSalary: Int = -1,
    val errorMessage: String = ""
)