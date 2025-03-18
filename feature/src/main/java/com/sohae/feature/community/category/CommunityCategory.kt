package com.sohae.feature.community.category

enum class CommunityCategory(
    val idx: Int,
    val categoryName: String,
    val subCategories: List<String>
) {
    ALL(
        0,
        "전체",
        listOf( "자유", "정보", "질문" )
    ),
    HOT(
        1,
        "인기",
        listOf( "일간", "주간", "월간" )
    ),
    NOTICE(
        2,
        "공지",
        listOf()
    )
}