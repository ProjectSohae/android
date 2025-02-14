package com.jhw.sohae.presentation.community

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

enum class CommunityCategories(
    val idx: Int,
    val categoryName: String,
    val list: List<CommunitySubCategory>
) {
    ALL(
        0,
        "전체",
        listOf(
            CommunitySubCategory(-1, ""),
            CommunitySubCategory(0, "자유"),
            CommunitySubCategory(1, "정보"),
            CommunitySubCategory(2, "질문")
        )
    ),
    HOT(
        1,
        "인기",
        listOf(
            CommunitySubCategory(0, "일간"),
            CommunitySubCategory(1, "주간"),
            CommunitySubCategory(2, "월간")
        )
    ),
    NOTICE(
        2,
        "공지",
        emptyList()
    )
}

data class CommunitySubCategory(
    val idx: Int,
    val name: String
)

class CommunityViewModel : ViewModel() {
    var savedStartDestination = CommunityCategories.ALL
    val postsList : List<Pair<String, String>> = emptyList()

    fun generateTest(count : Int) : List<Pair<String, String>> {
        val test = mutableListOf<Pair<String, String>>()

        for (idx : Int in 1..count) {
            test.add(
                Pair("코빈스의 이름은 코빈스의 이름은 코빈스의 이름은 코빈스의 이름은", "신준섭 신준섭 신준섭 신준섭 신준섭 신준섭")
            )
        }

        return test.toList()
    }
}