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

    val jeopsu_yy = mapOf(
        Pair("2021년", "2021"),
        Pair("2022년", "2022"),
        Pair("2023년", "2023"),
        Pair("2024년", "2024")
    )

    val jeopsu_tms = mapOf(
        Pair("재학생입영원", "1"),
        Pair("본인선택", "2")
    )

    val ghjbc_cd = mapOf(
        Pair("서울", "02"),
        Pair("부산•울산", "03"),
        Pair("대구•경북", "04"),
        Pair("경인", "05"),
        Pair("광주•전남", "06"),
        Pair("대전•충남", "07"),
        Pair("강원", "08"),
        Pair("충북", "09"),
        Pair("전북", "10"),
        Pair("경남", "11"),
        Pair("제주", "12"),
        Pair("인천", "13"),
        Pair("경기•북부", "14"),
        Pair( "강원•영동", "15")
    )

    val sortByList = listOf(
        "별점 낮은 순",
        "별점 높은 순"
    )
}