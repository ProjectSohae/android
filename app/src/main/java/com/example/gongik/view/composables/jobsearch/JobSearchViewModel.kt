package com.example.gongik.view.composables.jobsearch

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

enum class JobReviewScoreNameItem(
    val index: Int,
    val scoreName: String
) {
    CULTURE(0, "조직 문화"),
    WORKLIFEBALANCE(1, "업무와 삶의 균형"),
    BUILDING(2, "근무지 시설"),
    LOCATION(3, "근무지 위치"),
    RESTTIME(4, "휴가 사용")
}

val JobReviewScoreNamesList = listOf(
    JobReviewScoreNameItem.CULTURE.scoreName,
    JobReviewScoreNameItem.WORKLIFEBALANCE.scoreName,
    JobReviewScoreNameItem.BUILDING.scoreName,
    JobReviewScoreNameItem.LOCATION.scoreName,
    JobReviewScoreNameItem.RESTTIME.scoreName
)

val jobDetailsCategory = listOf(
    Pair("소집 일자", 120.dp),
    Pair("분류", 160.dp),
    Pair("복무 기관", 160.dp),
    Pair("최대 스택", 120.dp),
    Pair("최대 스택 경쟁률", 160.dp),
    Pair("전공자", 80.dp),
    Pair("공석", 80.dp),
    Pair("1지망 경쟁률", 120.dp),
    Pair("3회+", 80.dp),
    Pair("2회", 80.dp),
    Pair("1회", 80.dp),
    Pair("0회", 80.dp),
    Pair("훈련소", 120.dp)
)

class JobSearchViewModel : ViewModel() {

}