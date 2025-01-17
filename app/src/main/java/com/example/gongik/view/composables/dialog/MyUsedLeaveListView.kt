package com.example.gongik.view.composables.dialog

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.gongik.model.data.myinformation.MyUsedLeave

@Composable
fun MyUsedLeaveListView(
    myUsedLeaveList: List<MyUsedLeave>
) {
    myUsedLeaveList.forEach {
        Log.d("checkL", "$it")
    }
}