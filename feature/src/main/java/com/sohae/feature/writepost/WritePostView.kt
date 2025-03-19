package com.sohae.feature.writepost

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.sohae.common.resource.R
import com.sohae.common.ui.custom.composable.CircularLoadingBarView
import com.sohae.common.ui.custom.dialog.WheelPickerDialog
import com.sohae.common.ui.custom.snackbar.SnackBarBehindTarget
import com.sohae.common.ui.custom.snackbar.SnackBarController
import com.sohae.controller.mainnavgraph.MainNavGraphRoutes
import com.sohae.controller.mainnavgraph.MainNavGraphViewController
import com.sohae.controller.mainnavgraph.MainScreenController

@Composable
fun WritePostView(
    writePostViewModel: WritePostViewModel
){
    val mainNavController = MainNavGraphViewController.mainNavController
    val isReadyPostDetails = writePostViewModel.isReadyPostDetails.collectAsState().value

    LaunchedEffect(Unit) {

        if (writePostViewModel.postId != null) {

            writePostViewModel.loadPostDetails { isSucceed ->

                if (isSucceed) {
                    writePostViewModel.setIsReadyPostDetails(true)
                } else {
                    SnackBarController.show(
                        "게시글을 불러오는데 실패했습니다.\n다시 시도해 주세요.",
                        SnackBarBehindTarget.VIEW
                    )
                    mainNavController.popBackStack()
                }
            }
        } else {
            writePostViewModel.setIsReadyPostDetails(true)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        val innerPadding = PaddingValues(
            it.calculateLeftPadding(LayoutDirection.Rtl),
            it.calculateTopPadding(),
            it.calculateRightPadding(LayoutDirection.Rtl),
            0.dp
        )

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {
            WritePostViewHeader()

            if (!isReadyPostDetails) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularLoadingBarView()
                }
            } else {
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)) {
                    item { WritePostViewBody(writePostViewModel) }
                }

                WritePostViewFooter(writePostViewModel)
            }
        }
    }
}

@Composable
private fun WritePostViewHeader(
) {
    val mainNavController = MainNavGraphViewController.mainNavController

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 16.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { mainNavController.popBackStack() },
                contentDescription = null
            )
            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = "게시글 작성",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun WritePostViewBody(
    writePostViewModel: WritePostViewModel
) {
    Column {
        SelectCategory(writePostViewModel)

        WritePostTitleView(writePostViewModel)

        UploadPostImageListView(writePostViewModel)

        WritePostContentView(writePostViewModel)
    }
}

@Composable
private fun SelectCategory(
    writePostViewModel: WritePostViewModel
) {
    var isPressed by remember { mutableStateOf(false) }
    val selectedCategory = writePostViewModel.category.collectAsState().value
    val categoryList = writePostViewModel.categoryList

    if (isPressed) {
        WheelPickerDialog(
            hazeState = MainScreenController.hazeState,
            initIdx = selectedCategory.let {
                if (it < 0) { 0 } else { it }
            },
            intensity = 0.95f,
            onDismissRequest = { isPressed = false },
            onConfirm = { getSelectedCategory ->
                categoryList.forEachIndexed foreach@{ idx, item ->

                    if (item == getSelectedCategory.toString()) {
                        writePostViewModel.setCategory(idx)

                        return@foreach
                    }
                }

                isPressed = false
            },
            optionList = categoryList
        )
    }

    Column(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
    ) {
        Text(
            text = "카테고리",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(20),
                    color = selectedCategory.let {
                        if (it < 0) {
                            MaterialTheme.colorScheme.tertiary
                        } else {
                            Color.Transparent
                        }
                    },
                )
                .background(
                    color = selectedCategory.let {
                        if (it < 0) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    },
                    shape = RoundedCornerShape(20)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .clickable { isPressed = true },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedCategory.let {
                    if (it < 0) {
                        "카테고리 선택"
                    } else {
                        categoryList[selectedCategory]
                    }
                },
                fontSize = 16.sp,
                color = selectedCategory.let {
                    if (it < 0) {
                        MaterialTheme.colorScheme.tertiary
                    } else { MaterialTheme.colorScheme.onPrimary }
                }
            )
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                modifier = Modifier.size(20.dp),
                tint = selectedCategory.let {
                    if (it < 0) {
                        MaterialTheme.colorScheme.primary
                    } else { MaterialTheme.colorScheme.onPrimary }
                },
                contentDescription = null
            )
        }
    }
}

@Composable
private fun WritePostTitleView(
    writePostViewModel: WritePostViewModel
) {
    val title = writePostViewModel.title.collectAsState().value

    Column(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp)
    ) {
        Text(
            text = "제목",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = title,
            onValueChange = {
                if (it.length <= 50) { writePostViewModel.setTitle(it) }
            },
            placeholder = {
                Text(
                    text = "제목을 입력해주세요.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(15),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        Text(
            text = "${title.length}/50",
            fontSize = 16.sp,
            textAlign = TextAlign.End,
            color = title.isNotEmpty().let {
                if (it) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onPrimary
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun UploadPostImageListView(
    writePostViewModel: WritePostViewModel
) {
    val mainNavController = MainNavGraphViewController.mainNavController
    val selectedImageList = writePostViewModel.selectedImageList.collectAsState().value

    Column(
        modifier = Modifier
            .padding(start = 24.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "사진",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .size(64.dp)
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(15),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    .padding(top = 4.dp)
                    .clickable {
                        mainNavController.currentBackStackEntry?.savedStateHandle
                            ?.set("selected_image_list", selectedImageList)
                        mainNavController.navigate(MainNavGraphRoutes.SELECTIMAGE.name)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_photo_camera_24),
                    tint = MaterialTheme.colorScheme.tertiary,
                    contentDescription = null
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${selectedImageList.size}",
                        fontWeight = if (selectedImageList.isEmpty()) {
                            FontWeight.Normal
                        } else {
                            FontWeight.SemiBold
                        },
                        fontSize = 12.sp,
                        color = if (selectedImageList.isEmpty()) {
                            MaterialTheme.colorScheme.tertiary
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                    Text(
                        text = "/10",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }

            LazyRow(
                modifier = Modifier.padding(start = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    selectedImageList.forEachIndexed { idx: Int, uri ->
                        UploadPostImageItemView(
                            uri = uri,
                            onRemove = {
                                writePostViewModel.setSelectedImageList(
                                    selectedImageList - uri
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UploadPostImageItemView(
    uri: Uri,
    onRemove: () -> Unit
) {
    val tertiary = MaterialTheme.colorScheme.tertiary

    Box(
        modifier = Modifier.padding(end = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = uri,
            modifier = Modifier
                .padding(start = 1.dp)
                .size(64.dp)
                .drawWithContent {
                    drawContent()
                    drawOutline(
                        outline = Outline.Rounded(
                            roundRect = RoundRect(
                                0f,
                                0f,
                                this.size.width,
                                this.size.height,
                                CornerRadius(25f, 25f)
                            )
                        ),
                        color = tertiary,
                        style = Stroke(
                            width = (1.dp).toPx()
                        )
                    )
                }
                .clip(RoundedCornerShape(25f)),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        Icon(
            modifier = Modifier
                .size(20.dp)
                .offset(x = 30.dp, y = (-30).dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(100)
                )
                .rotate(45f)
                .clickable { onRemove() },
            painter = painterResource(id = R.drawable.baseline_add_24),
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = null
        )
    }
}

@Composable
private fun WritePostContentView(
    writePostViewModel: WritePostViewModel
) {
    val content = writePostViewModel.content.collectAsState().value

    Column(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
    ) {
        Text(
            text = "내용",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = content,
            onValueChange = {
                if (it.length <= 1000) { writePostViewModel.setContent(it) }
            },
            placeholder = {
                Text(
                    text = "게시글 내용을 작성해주세요." +
                            "\n\n욕설, 비방 등 상대방을 불쾌하게 하는 내용, 무단 홍보 게시글은 별도의 통보 없이 삭제될 수 있어요." +
                            "\n\n신고를 당하면 커뮤니티 이용이 제한될 수 있으니 주의해주세요.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(5),
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        )

        if (content.isNotEmpty()) {
            Text(
                text = "${content.length}/1000",
                fontSize = 16.sp,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun WritePostViewFooter(
    writePostViewModel: WritePostViewModel
) {
    val failure = { msg: String ->
        SnackBarController.show(msg, SnackBarBehindTarget.VIEW)
    }
    val mainNavController = MainNavGraphViewController.mainNavController
    val myId = writePostViewModel.myId.collectAsState().value?.id
    val category = writePostViewModel.category.collectAsState().value
    val title = writePostViewModel.title.collectAsState().value
    val content = writePostViewModel.content.collectAsState().value
    var enablePressButton by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(category, title, content) {

        if (
            category >= 0
            && title.isNotBlank()
            && content.isNotBlank()
        ) {
            enablePressButton = true
        } else {
            enablePressButton = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {

                if (myId != null) {

                    if (writePostViewModel.postId != null) {
                        writePostViewModel.updatePost(myId) { isSucceed ->

                            if (isSucceed) {
                                SnackBarController.show(
                                    "게시글을 수정했습니다.",
                                    SnackBarBehindTarget.VIEW
                                )
                                mainNavController.popBackStack()
                            } else {
                                SnackBarController.show(
                                    "게시글 수정에 문제가 발생했습니다.\n잠시 후 다시 시도해 주세요.",
                                    SnackBarBehindTarget.VIEW
                                )
                            }
                        }
                    } else {
                        writePostViewModel.uploadPost(myId) { msg, isSucceed ->

                            SnackBarController.show(msg, SnackBarBehindTarget.VIEW)

                            if (isSucceed) {
                                mainNavController.popBackStack()
                            }
                        }
                    }
                } else {
                    failure("계정에 문제가 발생했습니다.\n나중에 다시 시도해 주세요.")
                }
            },
            enabled = enablePressButton,
            shape = RoundedCornerShape(15),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.tertiary
            ),
            contentPadding = PaddingValues(vertical = 12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "작성 완료",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}