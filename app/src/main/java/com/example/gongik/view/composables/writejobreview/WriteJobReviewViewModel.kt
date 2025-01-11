package com.example.gongik.view.composables.writejobreview

import androidx.lifecycle.ViewModel

class WriteJobReviewViewModel: ViewModel() {
    val scoreName = listOf(
        "조직 문화",
        "업무와 삶의 균형",
        "근무지 시설",
        "근무지 위치",
        "휴가 사용"
    )
    val scoreValue = listOf(1, 2, 3, 4, 5)

}