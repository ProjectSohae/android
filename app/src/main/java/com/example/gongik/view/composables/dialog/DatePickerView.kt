package com.example.gongik.view.composables.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gongik.util.font.dpToSp
import com.example.gongik.util.function.getDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    title: String = "날짜 선택",
    initialSelectedDateMillis: Long,
    onDateSelected: (Long?) -> Unit,
    onDismissRequest: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = if (initialSelectedDateMillis < 0) { null } else { initialSelectedDateMillis },
        initialDisplayedMonthMillis = if (initialSelectedDateMillis < 0) { null } else { initialSelectedDateMillis }
    )
    var isDateSelected by remember { mutableStateOf(false) }

    LaunchedEffect(datePickerState.selectedDateMillis) {
        isDateSelected = datePickerState.selectedDateMillis?.let {
            it != initialSelectedDateMillis
        } ?: false
    }

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                enabled = isDateSelected,
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                }
            ) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("취소")
            }
        }
    ) {
        LazyColumn {
            item {
                DatePicker(
                    state = datePickerState,
                    title = {
                        Text(
                            text = title,
                            fontSize = dpToSp(dp = 20.dp),
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    title: String = "날짜 선택",
    initialSelectedStartDateMillis: Long,
    initialSelectedEndDateMillis: Long,
    onDateRangeSelected: (Long?, Long?) -> Unit,
    onDismissRequest: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = if (initialSelectedStartDateMillis < 0) { null } else { initialSelectedStartDateMillis },
        initialSelectedEndDateMillis = if (initialSelectedEndDateMillis < 0) { null } else { initialSelectedEndDateMillis }
    )
    var isDateSelected by remember { mutableStateOf(false) }

    LaunchedEffect(dateRangePickerState.selectedStartDateMillis) {
        isDateSelected = dateRangePickerState.selectedStartDateMillis?.let { true } ?: false
    }

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                enabled = isDateSelected,
                onClick = {
                    onDateRangeSelected(
                        dateRangePickerState.selectedStartDateMillis,
                        dateRangePickerState.selectedEndDateMillis
                    )
                }
            ) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("취소")
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(
                    text = title,
                    fontSize = dpToSp(dp = 20.dp),
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 4.dp)
                )
            },
            headline = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = dateRangePickerState.selectedStartDateMillis?.let {
                            getDate(it)
                        } ?: "",
                        fontSize = dpToSp(dp = 28.dp),
                        color = MaterialTheme.colorScheme.primary
                    )

                    dateRangePickerState.selectedEndDateMillis?.let {
                        if (it != dateRangePickerState.selectedStartDateMillis) {
                            Text(
                                text = " - ${getDate(it)}",
                                fontSize = dpToSp(dp = 28.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        )
    }
}