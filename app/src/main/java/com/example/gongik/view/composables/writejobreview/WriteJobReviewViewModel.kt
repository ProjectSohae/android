package com.example.gongik.view.composables.writejobreview

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WriteJobReviewViewModel: ViewModel() {
    val scoreName = listOf(
        "조직 문화",
        "업무와 삶의 균형",
        "근무지 시설",
        "근무지 위치",
        "휴가 사용"
    )

    private val _scoreValue = MutableStateFlow(
        listOf(0, 0, 0, 0, 0)
    )
    val scoreValue = _scoreValue.asStateFlow()

    fun avgScoreValue(): Float {
        var total = 0
        val scoreValueListSize = _scoreValue.value.size

        scoreValue.value.forEach { total += it }

        return (total / scoreValueListSize.toFloat())
    }

    fun updateScoreValue(idx: Int, value: Int) {
        val tmp: MutableList<Int> = _scoreValue.value.toMutableList()
        tmp[idx] = value
        _scoreValue.value = tmp
    }
}