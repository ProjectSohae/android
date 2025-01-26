package com.example.gongik.view.searchpost

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.Container
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gongik.R
import com.example.gongik.util.font.dpToSp
import com.example.gongik.view.main.MainNavGraphViewModel
import com.example.gongik.view.composables.snackbar.SnackBarBehindTarget
import com.example.gongik.view.composables.snackbar.SnackBarController
import com.example.gongik.view.composables.textfield.CustomTextFieldView

@Composable
fun SearchPostView(
    searchPostViewModel: SearchPostViewModel = viewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        val innerPadding = PaddingValues(
            it.calculateLeftPadding(LayoutDirection.Rtl),
            it.calculateTopPadding(),
            it.calculateRightPadding(LayoutDirection.Rtl),
            0.dp
        )

        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            SearchPostViewHeader(searchPostViewModel)

            SearchPostViewBody(searchPostViewModel)
        }
    }
}

@Composable
private fun SearchPostViewHeader(
    searchPostViewModel: SearchPostViewModel
) {
    val primary = MaterialTheme.colorScheme.primary
    val tertiary = MaterialTheme.colorScheme.tertiary
    val interactionSource = remember { MutableInteractionSource() }
    val isFocusedSearchBar = interactionSource.collectIsFocusedAsState().value
    val searchPostTitle = searchPostViewModel.searchPostTitle.collectAsState().value

    LaunchedEffect(isFocusedSearchBar) {

        if (isFocusedSearchBar) {
            searchPostViewModel.updateShowRecentSearchList(true)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = tertiary,
                    start = Offset(0f, this.size.height),
                    end = Offset(this.size.width, this.size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable { MainNavGraphViewModel.popBack() },
            contentDescription = null
        )

        CustomTextFieldView(
            modifier = Modifier.weight(1f),
            value = searchPostTitle,
            placeholder = "제목 검색",
            contentPadding = PaddingValues(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            onValueChange = {
                searchPostViewModel.updateSearchPostTitle(it)
                searchPostViewModel.updateShowRecentSearchList(true)
            }
        )

        Icon(
            painter = painterResource(id = R.drawable.outline_search_24),
            tint = primary,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 12.dp)
                .size(36.dp)
                .clickable(
                    indication = null,
                    interactionSource = null
                ) {
                    if (searchPostTitle.isNotBlank()) {
                        searchPostViewModel.request(
                            onFailure = { SnackBarController.show(it, SnackBarBehindTarget.VIEW) }
                        )
                    } else {
                        SnackBarController.show(
                            "검색어를 입력해주세요.",
                            SnackBarBehindTarget.VIEW
                        )
                    }
                }
        )
    }
}

@Composable
private fun SearchPostViewBody(
    searchPostViewModel: SearchPostViewModel
) {
    val showRecentSearchList = searchPostViewModel.showRecentSearchList.collectAsState().value

    Box(modifier = Modifier.fillMaxSize()) {
        // 검색 게시글 목록
        LazyColumn {
            item {
                Surface(modifier = Modifier.fillMaxSize()) {

                }
            }
        }

        // 최근 검색 목록
        if (showRecentSearchList) {
            LazyColumn {
                item {
                    RecentSearchListView(searchPostViewModel)
                }
            }
        }
    }
}

@Composable
private fun RecentSearchListView(
    searchPostViewModel: SearchPostViewModel
) {
    val recentSearchList = listOf(
        "sex","sex","sex","sex","sex","sex","sex","sex",
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(horizontal = 24.dp)
    ) {
        // 최근 검색 닫기 버튼
        if (searchPostViewModel.getRequestSearchPostTitle().isNotBlank()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "닫기",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = dpToSp(dp = 16.dp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        searchPostViewModel.updateShowRecentSearchList(false)
                    }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "최근 검색",
                fontWeight = FontWeight.SemiBold,
                fontSize = dpToSp(dp = 16.dp),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "모두 지우기",
                fontSize = dpToSp(dp = 14.dp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {

                }
            )
        }

        // 최근 검색 목록
        recentSearchList.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clickable {
                        searchPostViewModel.request(
                            inputSearchPostTitle = it,
                            onFailure = { errorMessage ->
                                SnackBarController.show(errorMessage, SnackBarBehindTarget.VIEW)
                            }
                        )
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_search_24),
                        tint = MaterialTheme.colorScheme.tertiary,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 16.dp)
                    )

                    Text(
                        text = it,
                        fontSize = dpToSp(dp = 16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.outline_clear_24),
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.clickable {

                    },
                    contentDescription = null
                )
            }
        }
    }
}