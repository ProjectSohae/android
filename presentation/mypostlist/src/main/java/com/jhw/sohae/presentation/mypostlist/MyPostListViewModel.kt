package com.jhw.sohae.presentation.mypostlist

import androidx.lifecycle.ViewModel

class MyPostListViewModel: ViewModel() {

    fun generateTest(n: Int) : List<String> {
        val res = mutableListOf<String>()

        for (idx: Int in 1..n) {
            res.add("")
        }

        return res.toList()
    }
}