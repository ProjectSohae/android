package com.sohae.common.ui.custom.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.sohae.common.ui.custom.R

@Composable
fun ProfileImage(
    modifier: Modifier,
    innerPadding: PaddingValues,
    imageUrl: String = ""
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(100))
    ) {
        if (imageUrl.isBlank()) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_profile_basic_icon_24),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(innerPadding),
                contentDescription = null
            )
        } else {

        }
    }
}