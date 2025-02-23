package com.sohae.domain.jobreview.entity

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