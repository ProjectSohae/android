package com.sohae.presentation.jobnavgraph

import androidx.lifecycle.ViewModel

enum class JobSearchCategory(
    val idx: Int,
    val categoryName: String
) {
    INFORMATION(0, "복무지 정보"),
    COMPRETITION(1, "이전 경쟁률"),
}

class JobInfoNavGraphViewModel : ViewModel() {

}