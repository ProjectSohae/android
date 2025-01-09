package com.example.gongik.model.data


val DISTRICT_URL_CURRENT_YEAR = "https://mwpt.mma.go.kr/caisBMHS/dmem/dmem/mwgr/shbm/selectSHBMGHJBCSiGunGu.json?hwamyeon_id=SHBMBISTJeopSuHHAN_M"
val STATUS_URL_CURRENT_YEAR   = "https://mwpt.mma.go.kr/caisBMHS/dmem/dmem/mwgr/shbm/selectSHBMBISTBMGGJeopSuHH.json?hwamyeon_id=SHBMBISTJeopSuHHAN_M"
val DISTRICT_URL_LAST_YEAR = "https://mwpt.mma.go.kr/caisBMHS/dmem/dmem/mwgr/shbm/selectSHBMGHJBCSiGunGu.json?hwamyeon_id=SHBMBISTCJJeopSuHHAN_M"
val STATUS_URL_LAST_YEAR   = "https://mwpt.mma.go.kr/caisBMHS/dmem/dmem/mwgr/shbm/selectSHBMBISTBMGGCJJeopSuHH.json?hwamyeon_id=SHBMBISTCJJeopSuHHAN_M"

val ghjbc_cd = mapOf(
    Pair("02", "서울"),
    Pair("03", "부산.울산"),
    Pair("04", "대구.경북"),
    Pair("05", "경인"),
    Pair("06", "광주.전남"),
    Pair("07", "대전.충남"),
    Pair("08", "강원"),
    Pair("09", "충북"),
    Pair("10", "전북"),
    Pair("11", "경남"),
    Pair("12", "제주"),
    Pair("13", "인천"),
    Pair("14", "경기북부"),
    Pair("15", "강원영동")
)

val column_names = mapOf(
    Pair("shbmsojip_dt", "소집일자"),
    Pair("seonbokmu_yn", "선복무"),
    Pair("ssggdbunryu_nm", "복무기관"),
    Pair("gsbaejeong_pcnt", "공석"),
    Pair("bokmu_ggm", "복무기관"),
    Pair("bistinweon", "1지망 선택인원"),
    Pair("jm1stinwon_talrak3", "탈락횟수별 1지망 선택인원 3회 이상"),
    Pair("jm1stinwon_talrak2", "탈락횟수별 1지망 선택인원 2회"),
    Pair("jm1stinwon_talrak1", "탈락횟수별 1지망 선택인원 1회"),
    Pair("jm1stinwon_talrak0", "탈락횟수별 1지망 선택인원 0회"),
    Pair("jm1stinwon_jeongong", "1지망 전공자 선택인원"),
    Pair("bistinweon2", "2지망 선택인원"),
    Pair("jm2stinwon_talrak3", "탈락횟수별 2지망 선택인원 3회 이상"),
    Pair("jm2stinwon_talrak2", "탈락횟수별 2지망 선택인원 2회"),
    Pair("jm2stinwon_talrak1", "탈락횟수별 2지망 선택인원 1회"),
    Pair("jm2stinwon_talrak0", "탈락횟수별 2지망 선택인원 0회"),
    Pair("jm2stinwon_jeongong", "2지망 전공자 선택인원"),
    Pair("budae_cdm", "입영부대"),
    Pair("bmgigwan_jhbh", "복무기관 전화번호"),
    Pair("shbjsiseol_sbjs", "세부주소")
)