package com.example.gongik.view.composables.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.gongik.controller.BarColorController
import com.example.gongik.util.font.dpToSp
import com.example.gongik.view.composables.main.MainNavGraphBarItems
import com.example.gongik.view.composables.main.MainNavGraphViewModel
import com.example.gongik.view.composables.snackbar.SnackBarBehindTarget
import com.example.gongik.view.composables.snackbar.SnackBarController

@Composable
fun PostView(
    postUid: Int?
) {
    BarColorController.setNavigationBarColor(MaterialTheme.colorScheme.onPrimary)

    if (postUid == null) {
        SnackBarController.show("잘못된 게시글 접근입니다.", SnackBarBehindTarget.VIEW)
        MainNavGraphViewModel.popBack()
    } else {
        Scaffold {
            val innerPadding = PaddingValues(
                it.calculateLeftPadding(LayoutDirection.Rtl),
                it.calculateTopPadding(),
                it.calculateRightPadding(LayoutDirection.Rtl),
                0.dp
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                PostViewHeader()

                PostViewBody(postUid)
            }
        }
    }
}

@Composable
private fun PostViewHeader(

) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "게시글",
            fontSize = dpToSp(dp = 24.dp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun PostViewBody(
    postUid: Int
) {
    Text(text = "$postUid", color = MaterialTheme.colorScheme.primary)
}