package com.jhw.sohae.data.model.jobinformation

data class JobInformation(
    // 시.군.구
    val Bjdsgg: String,
    // 시.군.구 코드
    val bjdsggjusoCd: Int,
    // 복무 기관 코드
    val bmgigwanCd: Int,
    // 복무 기관명
    val bokmuGgm: String,
    // 대표 복무 기관명
    val dpBokmuGgm: String,
    // 도로명 주소
    val drmJuso: String,
    // 관할 지방청 코드
    val ghjbcCd: String,
    // 관할 지방청명
    val gtcdNm: String,
    // 전화 번호
    val jeonhwaNo: String,
    // 선발 제한 질병
    val sbjhjilbyeong: String,
    // 성범죄 제한 여부
    val sbjjehanYn: String,
    // 소속 기관 대분류
    val ssggdaeBr: String,

    // 복무지 리뷰 총점
    val totalReviewScore: Float,
    // 복무지 리뷰 개수
    val totalReviewCount: Int
)