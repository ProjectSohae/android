package com.example.gongik.model.data

enum class JobReviewCategory(
    val idx: Int,
    val categoryName: String
) {
    INFORMATION(0, "복무지 정보"),
    REVIEW(1, "복무지 리뷰"),
}