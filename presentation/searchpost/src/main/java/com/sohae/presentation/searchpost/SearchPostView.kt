package com.sohae.presentation.searchpost

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sohae.common.resource.R
import com.sohae.common.ui.custom.snackbar.SnackBarBehindTarget
import com.sohae.common.ui.custom.snackbar.SnackBarController
import com.sohae.common.ui.custom.textfield.CustomTextFieldView
import com.sohae.controller.mainnavgraph.MainNavController
import com.sohae.domain.myinformation.entity.MySearchHistoryEntity

@Composable
fun SearchPostView(
    searchPostViewModel: SearchPostViewModel
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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.onPrimary)
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
                .clickable { MainNavController.popBack() },
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
                        searchPostViewModel.searchPostRequest(
                            onFailure = { SnackBarController.show(it, SnackBarBehindTarget.VIEW) }
                        )
                    } else {
                        SnackBarController.show("검색어를 입력해주세요.", SnackBarBehindTarget.VIEW)
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
            }
        }

        // 최근 검색 목록
        if (showRecentSearchList) {
            RecentSearchHistoryListView(searchPostViewModel)
        }
    }
}

@Composable
private fun RecentSearchHistoryListView(
    searchPostViewModel: SearchPostViewModel
) {
    val recentMySearchHistoryList = searchPostViewModel
        .recentMySearchHistoryList.collectAsState()
        .value.sortedBy { -it.id }

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
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        searchPostViewModel.updateShowRecentSearchList(false)
                        searchPostViewModel.initSearchPostTitle()
                    }
                )
            }
        }

        if (recentMySearchHistoryList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "최근 검색어가 없습니다.",
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "최근 검색",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "모두 지우기",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        searchPostViewModel.deleteMySearchHistory()
                    }
                )
            }

            // 최근 검색 목록
            LazyColumn {
                itemsIndexed(
                    items = recentMySearchHistoryList,
                    key = { idx: Int, item: MySearchHistoryEntity -> idx }
                ) { idx: Int, item: MySearchHistoryEntity ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clickable {
                                searchPostViewModel.searchPostRequest(
                                    inputSearchPostTitle = item.keyword,
                                    onFailure = { errorMessage ->
                                        SnackBarController.show(errorMessage, SnackBarBehindTarget.VIEW)
                                    }
                                )
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_search_24),
                                tint = MaterialTheme.colorScheme.tertiary,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 16.dp)
                            )

                            Text(
                                text = item.keyword,
                                fontSize = 16.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.outline_clear_24),
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.clickable {
                                searchPostViewModel.deleteMySearchHistory(item.id)
                            },
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}