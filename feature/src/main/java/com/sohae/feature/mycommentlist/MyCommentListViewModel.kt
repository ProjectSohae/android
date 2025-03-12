package com.sohae.feature.mycommentlist

import androidx.lifecycle.ViewModel

class MyCommentListViewModel: ViewModel() {

    fun generateTest(n: Int) : List<String> {
        val res = mutableListOf<String>()

        for (idx: Int in 1..n) {
            res.add("")
        }

        return res.toList()
    }
}