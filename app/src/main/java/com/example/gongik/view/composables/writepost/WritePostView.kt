package com.example.gongik.view.composables.writepost

import androidx.compose.foundation.Image
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gongik.R
import com.example.gongik.util.font.dpToSp
import com.example.gongik.view.composables.dialog.WheelPickerDialog
import com.example.gongik.view.composables.main.MainNavGraphViewModel

@Composable
fun WritePostView(
    writePostViewModel: WritePostViewModel = viewModel()
){
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
            WritePostViewHeader()

            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .weight(1f)) {
                item { WritePostViewBody(writePostViewModel) }
            }

            WritePostViewFooter(writePostViewModel)
        }
    }
}

@Composable
private fun WritePostViewHeader(
) {
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
                modifier = Modifier.clickable { MainNavGraphViewModel.popBack() },
                contentDescription = null
            )
            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = "게시글 작성",
                fontSize = dpToSp(dp = 20.dp),
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

        WritePostTitle(writePostViewModel)

        UploadPostImage()

        WritePostContent(writePostViewModel)
    }
}

@Composable
private fun SelectCategory(
    writePostViewModel: WritePostViewModel
) {
    var isPressed by remember { mutableStateOf(false) }
    var selectedCategory by rememberSaveable { mutableStateOf("") }
    val categoryList = listOf( "자유", "정보", "질문" )

    LaunchedEffect(selectedCategory) {

        if (selectedCategory.isBlank()) {
            writePostViewModel.setIsCategorySelected(false)
        } else {
            writePostViewModel.setIsCategorySelected(true)
        }
    }

    if (isPressed) {
        WheelPickerDialog(
            initIdx = when (selectedCategory) {
                categoryList[0] -> { 0 }
                categoryList[1] -> { 1 }
                categoryList[2] -> { 2 }
                else -> { 0 }
            },
            intensity = 0.95f,
            onDismissRequest = { isPressed = false },
            onConfirmation = { getSelectedCategory ->
                selectedCategory = getSelectedCategory.toString()
                isPressed = false
            },
            optionsList = categoryList
        )
    }

    Column(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
    ) {
        Text(
            text = "카테고리",
            fontSize = dpToSp(dp = 16.dp),
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
                        if (it.isBlank()) {
                            MaterialTheme.colorScheme.tertiary
                        } else { Color.Transparent }
                    },
                )
                .background(
                    color = selectedCategory.let {
                        if (it.isBlank()) {
                            MaterialTheme.colorScheme.onPrimary
                        } else { MaterialTheme.colorScheme.primary }
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
                    it.ifBlank { "카테고리 선택" }
                },
                fontSize = dpToSp(dp = 16.dp),
                color = selectedCategory.let {
                    if (it.isBlank()) {
                        MaterialTheme.colorScheme.tertiary
                    } else { MaterialTheme.colorScheme.onPrimary }
                }
            )
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                modifier = Modifier.size(20.dp),
                tint = selectedCategory.let {
                    if (it.isBlank()) {
                        MaterialTheme.colorScheme.primary
                    } else { MaterialTheme.colorScheme.onPrimary }
                },
                contentDescription = null
            )
        }
    }
}

@Composable
private fun WritePostTitle(
    writePostViewModel: WritePostViewModel
) {
    var title by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(title) {

        if (title.isBlank()) {
            writePostViewModel.setIsTitleBlank(true)
        } else {
            writePostViewModel.setIsTitleBlank(false)
        }
    }

    Column(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
    ) {
        Text(
            text = "제목",
            fontSize = dpToSp(dp = 16.dp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = title,
            onValueChange = {
                if (it.length <= 50) { title = it }
            },
            placeholder = {
                Text(
                    text = "제목을 입력해주세요.",
                    fontSize = dpToSp(dp = 16.dp),
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

        if (title.isNotEmpty()) {
            Text(
                text = "${title.length}/50",
                fontSize = dpToSp(dp = 16.dp),
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun UploadPostImage(

) {
    val tertiary = MaterialTheme.colorScheme.tertiary

    Column(
        modifier = Modifier
            .padding(start = 24.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "사진",
            fontSize = dpToSp(dp = 16.dp),
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
                    .padding(top = 4.dp),
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
                        text = "0",
                        fontSize = dpToSp(dp = 12.dp),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "/10",
                        fontSize = dpToSp(dp = 12.dp),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }

            LazyRow(
                modifier = Modifier.padding(start = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    for (idx:Int in 0..9) {
                        Box(
                            modifier = Modifier.padding(end = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
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
                                painter = painterResource(id = R.drawable.yoon),
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
                                    .rotate(45f),
                                painter = painterResource(id = R.drawable.baseline_add_24),
                                tint = MaterialTheme.colorScheme.onPrimary,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WritePostContent(
    writePostViewModel: WritePostViewModel
) {
    var content by remember { mutableStateOf("") }

    LaunchedEffect(content) {

        if (content.isBlank()) {
            writePostViewModel.setIsContentBlank(true)
        } else {
            writePostViewModel.setIsContentBlank(false)
        }
    }

    Column(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
    ) {
        Text(
            text = "내용",
            fontSize = dpToSp(dp = 16.dp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = content,
            onValueChange = {
                if (it.length <= 1000) { content = it }
            },
            placeholder = {
                Text(
                    text = "게시글 내용을 작성해주세요." +
                            "\n\n욕설, 비방 등 상대방을 불쾌하게 하는 내용, 무단 홍보 게시글은 별도의 통보 없이 삭제될 수 있어요." +
                            "\n\n신고를 당하면 커뮤니티 이용이 제한될 수 있으니 주의해주세요.",
                    fontSize = dpToSp(dp = 16.dp),
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
                .wrapContentHeight()
        )

        if (content.isNotEmpty()) {
            Text(
                text = "${content.length}/1000",
                fontSize = dpToSp(dp = 16.dp),
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
    val isCategorySelected = writePostViewModel.isCategorySelected.collectAsState().value
    val isTitleBlank = writePostViewModel.isTitleBlank.collectAsState().value
    val isContentBlank = writePostViewModel.isContentBlank.collectAsState().value
    var enablePressButton by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isCategorySelected, isTitleBlank, isContentBlank) {

        if (
            isCategorySelected
            && !isTitleBlank
            && !isContentBlank
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
                fontSize = dpToSp(dp = 20.dp),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}