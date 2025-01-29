package com.example.sohae.view.writejobreview

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

    val workTimeList = listOf(
        "주간 근무",
        "3조 2교대",
        "3조 3교대",
        "4조 2교대",
        "4조 3교대",
        "4조 4교대",
        "합숙 근무"
    )

    val needWearWorkSuitList = listOf(
        "미착용",
        "상황별 착용",
        "상시 착용"
    )

    val allWorkerCountList = listOf(
        "1명",
        "5명 이하",
        "10명 이하",
        "10명 이상"
    )

    private val _scoreValue = MutableStateFlow(
        listOf(0, 0, 0, 0, 0)
    )
    val scoreValue = _scoreValue.asStateFlow()

    private var _isReadyScoreAboutJob = MutableStateFlow(false)
    val isReadyScoreAboutJob = _isReadyScoreAboutJob.asStateFlow()

    private var _isReadyWorkDetails = MutableStateFlow(false)
    val isReadyWorkDetails = _isReadyWorkDetails.asStateFlow()

    private var _isReadyJobAdvantages = MutableStateFlow(false)
    val isReadyJobAdvantages = _isReadyJobAdvantages.asStateFlow()

    private var _isReadyJobDisadvantages = MutableStateFlow(false)
    val isReadyJobDisadvantages = _isReadyJobDisadvantages.asStateFlow()

    private var _isReadyJobOthers = MutableStateFlow(false)
    val isReadyJobOthers = _isReadyJobOthers.asStateFlow()

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

    fun updateIsReadyScoreAboutJob(input: Boolean) {
        _isReadyScoreAboutJob.value = input
    }

    fun updateIsReadyWorkDetails(input: Boolean) {
        _isReadyWorkDetails.value = input
    }

    fun updateIsReadyJobAdvantages(input: Boolean) {
        _isReadyJobAdvantages.value = input
    }

    fun updateIsReadyJobDisadvantages(input: Boolean) {
        _isReadyJobDisadvantages.value = input
    }

    fun updateIsReadyJobOthers(input: Boolean) {
        _isReadyJobOthers.value = input
    }
}