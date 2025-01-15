package com.example.gongik.view.composables.writejobreview

import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.runBlocking

@Composable
fun WriteJobReviewView(
    writeJobReviewViewModel: WriteJobReviewViewModel = viewModel()
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
            WritePostViewHeader()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                item { WritePostViewBody(writeJobReviewViewModel) }
            }

            WritePostViewFooter(writeJobReviewViewModel)
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
                text = "복무지 리뷰 작성",
                fontSize = dpToSp(dp = 20.dp),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun WritePostViewBody(
    writeJobReviewViewModel: WriteJobReviewViewModel
) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = "서울교통공사",
            fontSize = dpToSp(dp = 28.dp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(
                text = "기관분류",
                fontSize = dpToSp(dp = 16.dp),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "공공기관",
                fontSize = dpToSp(dp = 20.dp),
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        SetScoreAboutJob(writeJobReviewViewModel)

        WriteWorkDetails(writeJobReviewViewModel)

        WriteJobAdvantages(writeJobReviewViewModel)

        WriteJobDisadvantages(writeJobReviewViewModel)

        WriteJobOthers(writeJobReviewViewModel)
    }
}

@Composable
private fun SetScoreAboutJob(
    writeJobReviewViewModel: WriteJobReviewViewModel
) {
    val scoreName = writeJobReviewViewModel.scoreName
    val scoreValue = writeJobReviewViewModel.scoreValue.collectAsState().value

    LaunchedEffect(scoreValue) {
        runBlocking {
            var checkAllScored = true

            scoreValue.forEach {
                if (it < 1) {
                    checkAllScored = false
                }
            }

            writeJobReviewViewModel.updateIsReadyScoreAboutJob(checkAllScored)
        }
    }

    Column(
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = writeJobReviewViewModel.avgScoreValue().toString(),
                fontSize = dpToSp(dp = 16.dp),
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "/5.0",
                fontSize = dpToSp(dp = 16.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }

        scoreName.forEachIndexed { idx, item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item,
                    fontSize = dpToSp(dp = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )

                Row {
                    for (cnt: Int in 0..<5) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_star_24),
                            tint = if (cnt < scoreValue[idx]) {
                                Color(0xFFFDCC0D)
                            } else { MaterialTheme.colorScheme.tertiary },
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    writeJobReviewViewModel.updateScoreValue(idx, cnt + 1)
                                },
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WriteWorkDetails(
    writeJobReviewViewModel: WriteJobReviewViewModel
) {
    var workDetails by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(workDetails) {

        if (workDetails.isNotBlank()
            && workDetails.length > 9)
        {
            writeJobReviewViewModel.updateIsReadyWorkDetails(true)
        } else {
            writeJobReviewViewModel.updateIsReadyWorkDetails(false)
        }
    }

    Column(
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        Text(
            text = "주요 업무",
            fontSize = dpToSp(dp = 16.dp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = workDetails,
            onValueChange = { if (it.length <= 200) { workDetails = it } },
            placeholder = {
                Text(
                    text = "최소 10자 이상 입력해주세요.",
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

        if (workDetails.isNotEmpty()) {
            Text(
                text = "${workDetails.length}/200",
                fontSize = dpToSp(dp = 16.dp),
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun WriteJobAdvantages(
    writeJobReviewViewModel: WriteJobReviewViewModel
) {
    var jobAdvantages by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(jobAdvantages) {

        if (jobAdvantages.isNotBlank()
            && jobAdvantages.length > 4)
        {
            writeJobReviewViewModel.updateIsReadyJobAdvantages(true)
        } else {
            writeJobReviewViewModel.updateIsReadyJobAdvantages(false)
        }
    }

    Column(
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        Text(
            text = "장점",
            fontSize = dpToSp(dp = 16.dp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = jobAdvantages,
            onValueChange = { if (it.length <= 500) { jobAdvantages = it } },
            placeholder = {
                Text(
                    text = "최소 5자 이상 입력해주세요.",
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

        if (jobAdvantages.isNotEmpty()) {
            Text(
                text = "${jobAdvantages.length}/500",
                fontSize = dpToSp(dp = 16.dp),
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun WriteJobDisadvantages(
    writeJobReviewViewModel: WriteJobReviewViewModel
) {
    var jobDisadvantages by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(jobDisadvantages) {

        if (jobDisadvantages.isNotBlank()
            && jobDisadvantages.length > 4)
        {
            writeJobReviewViewModel.updateIsReadyJobDisadvantages(true)
        } else {
            writeJobReviewViewModel.updateIsReadyJobDisadvantages(false)
        }
    }

    Column(
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        Text(
            text = "단점",
            fontSize = dpToSp(dp = 16.dp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = jobDisadvantages,
            onValueChange = { if (it.length <= 500) { jobDisadvantages = it } },
            placeholder = {
                Text(
                    text = "최소 5자 이상 입력해주세요.",
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

        if (jobDisadvantages.isNotEmpty()) {
            Text(
                text = "${jobDisadvantages.length}/500",
                fontSize = dpToSp(dp = 16.dp),
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun WriteJobOthers(
    writeJobReviewViewModel: WriteJobReviewViewModel
) {
    var workTime by rememberSaveable { mutableStateOf("") }
    var needWearWorkSuit by rememberSaveable { mutableStateOf("") }
    var allWorkerCount by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(
        workTime,
        needWearWorkSuit,
        allWorkerCount
    ) {

        if (workTime.isNotBlank()
            && needWearWorkSuit.isNotBlank()
            && allWorkerCount.isNotBlank())
        {
            writeJobReviewViewModel.updateIsReadyJobOthers(true)
        } else {
            writeJobReviewViewModel.updateIsReadyJobOthers(false)
        }
    }

    Column {
        Column(
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(
                text = "근무 시간",
                fontSize = dpToSp(dp = 16.dp),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            WriteJobOthersItem(
                "근무 시간 선택",
                workTime,
                writeJobReviewViewModel.workTimeList
            ) { workTime = it }
        }

        Column(
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(
                text = "근무복 착용 여부",
                fontSize = dpToSp(dp = 16.dp),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            WriteJobOthersItem(
                "근무복 착용 여부 선택",
                needWearWorkSuit,
                writeJobReviewViewModel.needWearWorkSuitList
            ) { needWearWorkSuit = it }
        }

        Column(
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(
                text = "사회복무요원 수",
                fontSize = dpToSp(dp = 16.dp),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            WriteJobOthersItem(
                "사회복무요원 수 선택",
                allWorkerCount,
                writeJobReviewViewModel.allWorkerCountList
            ) { allWorkerCount = it }
        }
    }
}

@Composable
private fun WriteJobOthersItem(
    basicContent: String,
    selectedItemName: String,
    optionsList: List<String>,
    callback: (String) -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val initIdx = selectedItemName.let {
        var tmp = 0

        optionsList.forEachIndexed { idx, item ->
            if (it == item) { tmp = idx }
        }

        tmp
    }

    if (isPressed) {
        WheelPickerDialog(
            initIdx = initIdx,
            intensity = 0.8f,
            onDismissRequest = { isPressed = false },
            onConfirmation = { getItemName ->
                isPressed = false
                callback(getItemName.toString())
            },
            optionsList = optionsList
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(20),
                color = selectedItemName.let {
                    if (it.isBlank()) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        Color.Transparent
                    }
                },
            )
            .background(
                color = selectedItemName.let {
                    if (it.isBlank()) {
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
            text = selectedItemName.ifBlank { basicContent },
            fontSize = dpToSp(dp = 16.dp),
            color = selectedItemName.let {
                if (it.isBlank()) {
                    MaterialTheme.colorScheme.tertiary
                } else { MaterialTheme.colorScheme.onPrimary }
            }
        )
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
            modifier = Modifier.size(20.dp),
            tint = selectedItemName.let {
                if (it.isBlank()) {
                    MaterialTheme.colorScheme.primary
                } else { MaterialTheme.colorScheme.onPrimary }
            },
            contentDescription = null
        )
    }
}

@Composable
private fun WritePostViewFooter(
    writeJobReviewViewModel: WriteJobReviewViewModel
) {
    val isReadyScoreAboutJob = writeJobReviewViewModel.isReadyScoreAboutJob.collectAsState().value
    val isReadyWorkDetails = writeJobReviewViewModel.isReadyWorkDetails.collectAsState().value
    val isReadyJobAdvantages = writeJobReviewViewModel.isReadyJobAdvantages.collectAsState().value
    val isReadyJobDisadvantages = writeJobReviewViewModel.isReadyJobDisadvantages.collectAsState().value
    val isReadyJobOthers = writeJobReviewViewModel.isReadyJobOthers.collectAsState().value
    var enablePressButton by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(
        isReadyScoreAboutJob,
        isReadyWorkDetails,
        isReadyJobAdvantages,
        isReadyJobDisadvantages,
        isReadyJobOthers
    ) {
        enablePressButton = if (isReadyScoreAboutJob
            && isReadyWorkDetails
            && isReadyJobAdvantages
            && isReadyJobDisadvantages
            && isReadyJobOthers
        ) { true } else { false }
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