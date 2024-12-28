package com.example.gongik.view.composables.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gongik.util.font.dpToSp

@Composable
fun LazySelectDialog(
    onDissmissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
    title: String,
    optionsList: List<String> = listOf(
        "2021",
        "2022",
        "2023",
        "2024"
    )
) {
    val primary = MaterialTheme.colorScheme.primary
    val tertiary = MaterialTheme.colorScheme.tertiary

    Dialog(
        onDismissRequest = { onDissmissRequest() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(10)
                )
                .padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = dpToSp(dp = 20.dp),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = primary,
                            strokeWidth = 4f,
                            start = Offset(0f, this.size.height),
                            end = Offset(this.size.width, this.size.height)
                        )
                    }
                    .padding(bottom = 8.dp)
            )

            LazyColumn(
            ) {
                itemsIndexed(
                    items = optionsList,
                    key = { index: Int, item ->
                        index
                    }
                ) { index: Int, item ->
                    Text(
                        text = item,
                        fontSize = dpToSp(dp = 16.dp),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .drawBehind {
                                if (index > 0) {
                                    drawLine(
                                        color = tertiary,
                                        strokeWidth = 2f,
                                        start = Offset(this.size.width * 0.05f, 0f),
                                        end = Offset(this.size.width * 0.95f, 0f)
                                    )
                                }
                            }
                            .padding(top = 8.dp)
                            .padding(bottom = 8.dp)
                            .clickable { onConfirmation(item) }
                    )
                }
            }
        }
    }
}