package com.sohae.feature.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.Container
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sohae.common.resource.R
import com.sohae.common.ui.custom.dialog.DatePickerDialog
import com.sohae.common.ui.custom.dialog.DateRangePickerDialog
import com.sohae.common.ui.custom.snackbar.SnackBarBehindTarget
import com.sohae.common.ui.custom.snackbar.SnackBarController
import com.sohae.common.ui.custom.snackbar.SnackbarView
import com.sohae.controller.mainnavgraph.MainScreenController
import com.sohae.domain.myinformation.entity.MyUsedLeaveEntity
import com.sohae.domain.utils.getDate
import com.sohae.domain.utils.getLeavePeriod
import dev.chrisbanes.haze.HazeEffectScope
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UsingMyLeaveView(
    myUsedLeave: com.sohae.domain.myinformation.entity.MyUsedLeaveEntity? = null,
    title: String,
    leaveKindIdx: Int,
    leaveTypeList: List<String>,
    onDismissRequest: () -> Unit,
    onConfirm: (com.sohae.domain.myinformation.entity.MyUsedLeaveEntity) -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val tertiary = MaterialTheme.colorScheme.tertiary
    var entityId by remember { mutableIntStateOf(0) }
    var selectedLeaveTypeIdx by remember { mutableIntStateOf(-1) }
    var reasonUsingLeave by remember { mutableStateOf("") }
    var leavePeriod by remember { mutableLongStateOf(-1) }
    var leaveStartDate by remember { mutableLongStateOf(-1) }
    var leaveEndDate by remember { mutableLongStateOf(-1) }
    val welfareOptionsList = listOf("미지급", "지급")
    // 0: 미지급, 1: 지급
    var receiveLunchSupport by remember { mutableIntStateOf(0) }
    // 0: 미지급, 1: 지급
    var receiveTransportationSupport by remember { mutableIntStateOf(0) }
    val interactionSource = remember { MutableInteractionSource() }
    val textFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
        disabledIndicatorColor = MaterialTheme.colorScheme.tertiary,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent
    )
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        SnackBarController.hide()

        if (myUsedLeave != null) {
            entityId = myUsedLeave.id
            selectedLeaveTypeIdx = myUsedLeave.leaveTypeIdx
            reasonUsingLeave = myUsedLeave.reason
            leavePeriod = myUsedLeave.usedLeaveTime
            leaveStartDate = myUsedLeave.leaveStartDate
            leaveEndDate = myUsedLeave.leaveEndDate
            receiveLunchSupport = if (myUsedLeave.receiveLunchSupport) { 1 } else { 0 }
            receiveTransportationSupport = if (myUsedLeave.receiveTransportationSupport) { 1 } else { 0 }
        }
    }

    if (showDatePicker) {
        if (selectedLeaveTypeIdx >= 0) {
            if (leaveTypeList[selectedLeaveTypeIdx] == "연차") {
                DateRangePickerDialog(
                    title = "${leaveTypeList[selectedLeaveTypeIdx]} 기간 선택",
                    initialSelectedStartDateMillis = leaveStartDate,
                    initialSelectedEndDateMillis = leaveEndDate,
                    onDateRangeSelected = { getStartDate, getEndDate ->
                        leaveStartDate = getStartDate ?: (-1)
                        leaveEndDate = getEndDate ?: (-1)

                        if (leaveStartDate > 0) {

                            leavePeriod = if (leaveEndDate > 0) {
                                (ChronoUnit.DAYS.between(
                                    LocalDateTime.ofInstant(
                                        Instant.ofEpochMilli(leaveStartDate),
                                        ZoneId.systemDefault()
                                    ),
                                    LocalDateTime.ofInstant(
                                        Instant.ofEpochMilli(leaveEndDate),
                                        ZoneId.systemDefault()
                                    )
                                ) + 1) * (1000 * 60 * 60 * 8)
                            } else {
                                (1000 * 60 * 60 * 8)
                            }
                        }

                        showDatePicker = false
                    },
                    onDismissRequest = {
                        showDatePicker = false
                    }
                )
            } else {
                DatePickerDialog(
                    title = "${leaveTypeList[selectedLeaveTypeIdx]} 날짜 선택",
                    initialSelectedDateMillis = leaveStartDate,
                    onDateSelected = { getStartDate ->
                        leaveStartDate = getStartDate ?: (-1)
                        showDatePicker = false
                    },
                    onDismissRequest = {
                        showDatePicker = false
                    }
                )
            }
        } else {
            SnackBarController.show("휴가 종류를 선택해 주세요.", SnackBarBehindTarget.DIALOG)
            showDatePicker = false
        }
    }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = SnackBarController.snackbarHostState
                ) { getSnackbarData ->
                    if (SnackBarController.behindTarget == SnackBarBehindTarget.DIALOG) {
                        SnackBarController.currentSnackbar = getSnackbarData
                        SnackbarView(snackbarData = getSnackbarData)
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxHeight()
                    .clickable(
                        indication = null,
                        interactionSource = null
                    ) { onDismissRequest() },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .shadow(20.dp, RoundedCornerShape(15))
                        .clip(RoundedCornerShape(15))
                        .hazeEffect(state = MainScreenController.hazeState,
                            style = HazeStyle(
                                backgroundColor = onPrimary,
                                tint = HazeTint(color = onPrimary),
                                blurRadius = 25.dp
                            ),
                            block = fun HazeEffectScope.() {
                                progressive = HazeProgressive.LinearGradient(
                                    startIntensity = 0.95f,
                                    endIntensity = 0.95f,
                                    preferPerformance = true
                                )
                            })
                        .clickable(
                            indication = null,
                            interactionSource = null
                        ) {}
                        .padding(top = 12.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
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
                            .padding(bottom = 12.dp),
                    )

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f, false)
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 휴가 종류
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            ) {
                                Text(
                                    text = "휴가 종류",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )

                                LazyRow {
                                    item {
                                        leaveTypeList.forEachIndexed { idx, item ->
                                            Text(
                                                text = item,
                                                fontSize = 16.sp,
                                                color = if (idx == selectedLeaveTypeIdx) { onPrimary } else { tertiary },
                                                modifier = Modifier
                                                    .padding(end = 12.dp)
                                                    .drawBehind {
                                                        if (idx == selectedLeaveTypeIdx) {
                                                            drawRoundRect(
                                                                color = primary,
                                                                cornerRadius = CornerRadius(100f, 100f)
                                                            )
                                                        } else {
                                                            drawOutline(
                                                                outline = Outline.Rounded(
                                                                    roundRect = RoundRect(
                                                                        0f,
                                                                        0f,
                                                                        this.size.width,
                                                                        this.size.height,
                                                                        CornerRadius(100f, 100f)
                                                                    )
                                                                ),
                                                                color = tertiary,
                                                                style = Stroke(width = (1.dp).toPx())
                                                            )
                                                        }
                                                    }
                                                    .clickable {

                                                        if (item == "오전 반차") {
                                                            receiveLunchSupport = 0
                                                            receiveTransportationSupport = 1
                                                            leavePeriod = 1000 * 60 * 60 * 4
                                                        } else if (item == "오후 반차") {
                                                            receiveLunchSupport = 1
                                                            receiveTransportationSupport = 1
                                                            leavePeriod = 1000 * 60 * 60 * 4
                                                        } else if (
                                                            item.contains("지각")
                                                            || item.contains("조퇴")
                                                            || item.contains("외출")
                                                        ) {
                                                            leavePeriod = 1000 * 60 * 10
                                                        } else {
                                                            leavePeriod = 1000 * 60 * 60 * 8
                                                        }

                                                        selectedLeaveTypeIdx = idx
                                                    }
                                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // 휴가 사유
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            ) {
                                Text(
                                    text = "휴가 사유",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )

                                BasicTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = reasonUsingLeave,
                                    onValueChange = { reasonUsingLeave = it },
                                    enabled = true,
                                    textStyle = TextStyle(
                                        fontSize =16.sp,
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false
                                        )
                                    ),
                                    interactionSource = interactionSource,
                                    singleLine = true,
                                    decorationBox = { innerTextField ->
                                        OutlinedTextFieldDefaults.DecorationBox(
                                            value = reasonUsingLeave,
                                            visualTransformation = VisualTransformation.None,
                                            innerTextField = innerTextField,
                                            contentPadding = PaddingValues(
                                                start = 12.dp,
                                                end = 12.dp,
                                                top = 8.dp,
                                                bottom = 8.dp
                                            ),
                                            placeholder = {
                                                Text(
                                                    text = "내용 입력",
                                                    fontSize = 16.sp,
                                                    color = MaterialTheme.colorScheme.tertiary,
                                                )
                                            },
                                            container = @Composable {
                                                Container(
                                                    enabled = true,
                                                    isError = false,
                                                    interactionSource = interactionSource,
                                                    modifier = Modifier,
                                                    colors = textFieldColors,
                                                    shape = RoundedCornerShape(25),
                                                    focusedBorderThickness = 2.dp,
                                                    unfocusedBorderThickness = 1.dp,
                                                )
                                            },
                                            singleLine = true,
                                            enabled = true,
                                            isError = false,
                                            interactionSource = interactionSource,
                                            colors = textFieldColors
                                        )
                                    }
                                )
                            }
                        }

                        // 휴가 기간
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            ) {
                                Text(
                                    text = "휴가 기간",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = if (leaveStartDate < 0) {
                                                Color.Transparent
                                            } else {
                                                primary
                                            },
                                            shape = RoundedCornerShape(25)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (leaveStartDate < 0) {
                                                tertiary
                                            } else {
                                                Color.Transparent
                                            },
                                            shape = RoundedCornerShape(25)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                        .clickable { showDatePicker = true },
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (leaveStartDate < 0) {
                                            "날짜 선택"
                                        } else {
                                            com.sohae.domain.utils.getDate(leaveStartDate) +
                                                    if (leaveEndDate < 0 || selectedLeaveTypeIdx > 0) {
                                                        ""
                                                    } else { "\n - ${
                                                        com.sohae.domain.utils.getDate(
                                                            leaveEndDate
                                                        )
                                                    }" }
                                        },
                                        fontSize = 16.sp,
                                        style = TextStyle(
                                            platformStyle = PlatformTextStyle(
                                                includeFontPadding = false
                                            )
                                        ),
                                        color = if (leaveStartDate < 0) { tertiary } else { onPrimary }
                                    )

                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                                        tint =  if (leaveStartDate < 0) {
                                            tertiary
                                        } else { onPrimary },
                                        modifier = Modifier.size(16.dp),
                                        contentDescription = null
                                    )
                                }

                                if (selectedLeaveTypeIdx >= 0) {
                                    if (leaveTypeList[selectedLeaveTypeIdx].contains("지각")
                                        || leaveTypeList[selectedLeaveTypeIdx].contains("조퇴")
                                        || leaveTypeList[selectedLeaveTypeIdx].contains("외출"))
                                    {
                                        Row(
                                            modifier = Modifier
                                                .padding(top = 6.dp)
                                                .fillMaxWidth(0.85f)
                                                .clip(RoundedCornerShape(25))
                                                .background(primary)
                                                .padding(vertical = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.baseline_remove_24),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .drawBehind {
                                                        drawLine(
                                                            color = onPrimary,
                                                            start = Offset(this.size.width, 0f),
                                                            end = Offset(this.size.width, this.size.height),
                                                            strokeWidth = 1.dp.toPx()
                                                        )
                                                    }
                                                    .clickable(
                                                        indication = null,
                                                        interactionSource = null
                                                    ) {
                                                        if (leavePeriod > 0) {
                                                            leavePeriod -= 1000 * 60 * 10
                                                        }
                                                    }
                                            )

                                            Text(
                                                text = com.sohae.domain.utils.getLeavePeriod(
                                                    leavePeriod
                                                ),
                                                fontSize = 16.sp,
                                                textAlign = TextAlign.Center,
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.weight(4f)
                                            )

                                            Icon(
                                                painter = painterResource(id = R.drawable.baseline_add_24),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .drawBehind {
                                                        drawLine(
                                                            color = onPrimary,
                                                            start = Offset(0f, 0f),
                                                            end = Offset(0f, this.size.height),
                                                            strokeWidth = 1.dp.toPx()
                                                        )
                                                    }
                                                    .clickable(
                                                        indication = null,
                                                        interactionSource = null
                                                    ) {
                                                        leavePeriod += 1000 * 60 * 10
                                                    }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // 휴가 중 식비 지급 여부
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            ) {
                                Text(
                                    text = "휴가 중 식비 지급 여부",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .drawBehind {
                                            drawOutline(
                                                outline = Outline.Rounded(
                                                    roundRect = RoundRect(
                                                        0f, 0f, this.size.width, this.size.height,
                                                        CornerRadius(100f, 100f)
                                                    )
                                                ),
                                                style = Stroke(width = 1.dp.toPx()),
                                                color = tertiary
                                            )
                                            drawRoundRect(
                                                topLeft = Offset(
                                                    (this.size.width / 2f) * receiveLunchSupport.toFloat(),
                                                    0f
                                                ),
                                                size = Size(this.size.width / 2f, this.size.height),
                                                color = primary,
                                                cornerRadius = CornerRadius(100f, 100f)
                                            )
                                        }
                                        .padding(vertical = 8.dp)
                                    ,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    welfareOptionsList.forEachIndexed { idx, item ->
                                        Text(
                                            text = item,
                                            fontSize = 16.sp,
                                            textAlign = TextAlign.Center,
                                            color = if (idx == receiveLunchSupport) {
                                                onPrimary
                                            } else { tertiary },
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable(indication = null, interactionSource = null) {
                                                    receiveLunchSupport = idx
                                                }
                                        )
                                    }
                                }
                            }
                        }

                        // 휴가 중 교통비 지급 여부
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp, bottom = 20.dp)
                            ) {
                                Text(
                                    text = "휴가 중 교통비 지급 여부",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .drawBehind {
                                            drawOutline(
                                                outline = Outline.Rounded(
                                                    roundRect = RoundRect(
                                                        0f, 0f, this.size.width, this.size.height,
                                                        CornerRadius(100f, 100f)
                                                    )
                                                ),
                                                style = Stroke(width = 1.dp.toPx()),
                                                color = tertiary
                                            )
                                            drawRoundRect(
                                                topLeft = Offset(
                                                    (this.size.width / 2f) * receiveTransportationSupport.toFloat(),
                                                    0f
                                                ),
                                                size = Size(this.size.width / 2f, this.size.height),
                                                color = primary,
                                                cornerRadius = CornerRadius(100f, 100f)
                                            )
                                        }
                                        .padding(vertical = 8.dp)
                                    ,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    welfareOptionsList.forEachIndexed { idx, item ->
                                        Text(
                                            text = item,
                                            fontSize = 16.sp,
                                            textAlign = TextAlign.Center,
                                            color = if (idx == receiveTransportationSupport) {
                                                onPrimary
                                            } else { tertiary },
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable(indication = null, interactionSource = null) {
                                                    receiveTransportationSupport = idx
                                                }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawBehind {
                                drawLine(
                                    color = tertiary,
                                    start = Offset(0f, 0f),
                                    end = Offset(this.size.width, 0f),
                                    strokeWidth = 1.dp.toPx()
                                )
                                drawLine(
                                    color = tertiary,
                                    start = Offset(this.size.width / 2f, 0f),
                                    end = Offset(this.size.width / 2f, this.size.height),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "취소",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    indication = null,
                                    interactionSource = null
                                ) { onDismissRequest() }
                        )

                        Text(
                            text = "확인",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = if (
                                selectedLeaveTypeIdx >= 0
                                && leaveStartDate >= 0
                                && leavePeriod > 0)
                            {
                                primary
                            } else { tertiary },
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    indication = null,
                                    interactionSource = null
                                ) {
                                    if (
                                        selectedLeaveTypeIdx >= 0
                                        && leaveStartDate >= 0
                                        && leavePeriod > 0)
                                    {
                                        if (leaveStartDate == leaveEndDate) {
                                            leaveEndDate = -1
                                        }

                                        onConfirm(
                                            com.sohae.domain.myinformation.entity.MyUsedLeaveEntity(
                                                id = entityId,
                                                leaveKindIdx = leaveKindIdx,
                                                leaveTypeIdx = selectedLeaveTypeIdx,
                                                reason = reasonUsingLeave,
                                                usedLeaveTime = leavePeriod,
                                                leaveStartDate = leaveStartDate,
                                                leaveEndDate = leaveEndDate,
                                                receiveLunchSupport = if (receiveLunchSupport < 1) {
                                                    false
                                                } else {
                                                    true
                                                },
                                                receiveTransportationSupport = if (receiveTransportationSupport < 1) {
                                                    false
                                                } else {
                                                    true
                                                }
                                            )
                                        )
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}