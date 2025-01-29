package com.example.sohae.model.data.jobinformation

data class JobApplyHistory(
    // 소집일자
    val shbmsojip_dt: String,
    // 선복무 여부
    val seonbokmu_yn: String,
    // 기관 분류
    val ssggdbunryu_nm: String,
    // 공석
    val gsbaejeong_pcnt: String,
    // 복무 기관
    val bokmu_ggm: String,
    // 1지망 선택 인원
    val bistinweon: String,
    // 1지망 3스택 지원자 수
    val jm1stinwon_talrak3: String,
    // 1지망 2스택 지원자 수
    val jm1stinwon_talrak2: String,
    // 1지망 1스택 지원자 수
    val jm1stinwon_talrak1: String,
    // 1지망 0스택 지원자 수
    val jm1stinwon_talrak0: String,
    // 1지망 전공자 지원자 수
    val jm1stinwon_jeongong: String,
    // 2지망 선택 인원
    val bistinweon2: String,
    // 2지망 3스택 지원자 수
    val jm2stinwon_talrak3: String,
    // 2지망 2스택 지원자 수
    val jm2stinwon_talrak2: String,
    // 2지망 1스택 지원자 수
    val jm2stinwon_talrak1: String,
    // 2지망 0스택 지원자 수
    val jm2stinwon_talrak0: String,
    // 2지망 전공자 지원자 수
    val jm2stinwon_jeongong: String,
    // 입영 부대, 선복무 일 시 내용 없음
    val budae_cdm: String,
    // 복무기관 전화번호
    val bmgigwan_jhbh: String,
    // 세부 주소
    val shbjsiseol_sbjs: String
)