package com.sohae.feature.selectimage

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import com.sohae.common.resource.R
import com.sohae.controller.navigation.main.MainNavGraphViewController
import com.sohae.controller.ui.MainScreenController
import com.sohae.controller.ui.snackbar.SnackBarBehindTarget
import com.sohae.controller.ui.snackbar.SnackBarController
import dev.chrisbanes.haze.HazeEffectScope
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

@Composable
fun SelectImageView(
    selectImageViewModel: SelectImageViewModel,
    onConfirm: (List<Uri>) -> Unit
) {
    val mainNavController = MainNavGraphViewController.mainNavController
    val context = LocalContext.current
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    var showAlert by remember { mutableStateOf(false) }
    var isGranted by rememberSaveable { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            isGranted = true
        } else {
            showAlert = true
        }
    }
    var imageUris by rememberSaveable { mutableStateOf<List<List<Uri>>>(emptyList()) }

    LaunchedEffect(Unit) {

        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            isGranted = true
        } else {
            permissionLauncher.launch(permission)
        }
    }

    LaunchedEffect(isGranted) {

        if (isGranted) {
            selectImageViewModel.getAllImages(context) {
                imageUris = it
            }
            isGranted = false
        }
    }

    if (showAlert) {

        RequestPermissionView(
            onConfirm = {
                try {
                    context.startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        ).apply {
                            data = "package:${context.packageName}".toUri()
                        }
                    )
                } catch (e: ActivityNotFoundException) {
                    SnackBarController.show(
                        "수행할 수 있는 컴포넌트가 없습니다.",
                        SnackBarBehindTarget.VIEW
                    )
                }
                mainNavController.popBackStack()
            },
            onFailure = {
                mainNavController.popBackStack()
            }
        )
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
                .background(MaterialTheme.colorScheme.onPrimary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SelectImageHeaderView(
                selectImageViewModel = selectImageViewModel,
                onConfirm = { selectedImageList ->
                    onConfirm(selectedImageList)
                }
            )

            SelectImageBodyView(
                imageUris = imageUris,
                selectImageViewModel = selectImageViewModel
            )
        }
    }
}

@Composable
private fun SelectImageHeaderView(
    selectImageViewModel: SelectImageViewModel,
    onConfirm: (List<Uri>) -> Unit
) {
    val mainNavController = MainNavGraphViewController.mainNavController
    val isSelectedImageListChanged = selectImageViewModel.isSelectedImageListChanged.collectAsState().value
    val selectedImageList = selectImageViewModel.selectedImagesList.collectAsState().value

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
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
                text = "사진 선택",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = "완료",
            fontSize = 20.sp,
            fontWeight = if (isSelectedImageListChanged) {
                FontWeight.SemiBold
            } else { FontWeight.Normal },
            color = if (isSelectedImageListChanged) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.tertiary
            },
            modifier = Modifier.clickable {

                if (isSelectedImageListChanged) {
                    onConfirm(selectedImageList)
                }
            }
        )
    }
}

@Composable
private fun SelectImageBodyView(
    imageUris: List<List<Uri>>,
    selectImageViewModel: SelectImageViewModel
) {
    val selectedImageList = selectImageViewModel.selectedImagesList.collectAsState().value

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(2.dp)
    ) {
        itemsIndexed(
            items = imageUris,
            key = { rowIdx, item -> rowIdx }
        ) { rowIdx, item ->
            if (imageUris[rowIdx].isNotEmpty()) {
                val columnSize = imageUris[rowIdx].size

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (columnIdx: Int in 0..<columnSize) {
                        val imageUri = imageUris[rowIdx][columnIdx]

                        SelectImageItemView(
                            modifier = Modifier.fillParentMaxWidth(0.33f),
                            uri = imageUri,
                            isSelected = selectedImageList.contains(imageUri),
                            selectedIdx = selectedImageList.indexOf(imageUri) + 1,
                            onClick = {
                                selectImageViewModel.selectImage(imageUri).let callback@{

                                    if (it.isNotBlank()) {
                                        SnackBarController.show(it, SnackBarBehindTarget.VIEW)

                                        return@callback false
                                    }

                                    return@callback true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectImageItemView(
    modifier: Modifier,
    uri: Uri,
    isSelected: Boolean,
    selectedIdx: Int,
    onClick: () -> Boolean
) {
    val primary = MaterialTheme.colorScheme.primary
    var showSelectedDeco by remember { mutableStateOf(isSelected) }
    val selectedFrame by animateFloatAsState(
        targetValue = if (isSelected) { 1f } else { 0f }
    ) {
        if (!isSelected) {
            showSelectedDeco = false
        }
    }
    val getRatio: (Float, Float) -> Float = { value, paddingValue ->
        (value - paddingValue) / value
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f, true)
            .padding(2.dp)
            .clickable(
                interactionSource = null,
                indication = null
            ) {
                onClick().let {

                    if (showSelectedDeco != it) {
                        showSelectedDeco = it
                    }
                }
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .crossfade(true)
                .allowHardware(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        if (showSelectedDeco) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        drawContent()

                        val paddingValue = 2.dp.toPx()
                        val width = this.size.width
                        val widthRatio = getRatio(width, paddingValue)
                        val height = this.size.height
                        val heightRatio = getRatio(height, paddingValue)

                        drawOutline(
                            outline = Outline.Rectangle(
                                rect = Rect(
                                    offset = Offset(
                                        paddingValue * selectedFrame / 2f,
                                        paddingValue * selectedFrame / 2f
                                    ),
                                    size = Size(
                                        width - ((width * (1f - widthRatio)) * selectedFrame),
                                        height - ((height * (1f - heightRatio)) * selectedFrame)
                                    )
                                )
                            ),
                            color = primary,
                            style = Stroke(width = 4.dp.toPx() * selectedFrame),
                            alpha = selectedFrame
                        )
                    }
                    .graphicsLayer {
                        scaleX = getRatio(this.size.width, 2.dp.toPx())
                        scaleY = getRatio(this.size.height, 2.dp.toPx())

                        alpha = 0.5f * selectedFrame
                    },
                color = MaterialTheme.colorScheme.secondary
            ) {}

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 6.dp, end = 6.dp)
                    .fillMaxSize(0.2f)
                    .drawBehind {

                        drawRoundRect(
                            color = primary,
                            topLeft = Offset(
                                (this.size.width / 2f) * (1f - selectedFrame),
                                (this.size.height / 2f) * (1f - selectedFrame)
                            ),
                            size = Size(
                                this.size.width * selectedFrame,
                                this.size.height * selectedFrame
                            ),
                            cornerRadius = CornerRadius(100f, 100f)
                        )
                    }
                    .graphicsLayer {
                        scaleX = selectedFrame
                        scaleY = selectedFrame
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = selectedIdx.let { if (it < 1) { "" } else { it } }.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            }
        }
    }
}

@Composable
private fun RequestPermissionView(
    hazeState: HazeState = MainScreenController.hazeState,
    intensity: Float = 0.85f,
    onConfirm: () -> Unit,
    onFailure: () -> Unit
) {
    val tertiary = MaterialTheme.colorScheme.tertiary

    Dialog(
        onDismissRequest = onFailure
    ) {
        Column(
            modifier = Modifier
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(10)
                )
                .clip(RoundedCornerShape(10))
                .hazeEffect(
                    state = hazeState,
                    style = HazeStyle(
                        backgroundColor = MaterialTheme.colorScheme.onPrimary,
                        tint = HazeTint(
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        blurRadius = 25.dp
                    ),
                    block = fun HazeEffectScope.() {
                        progressive = HazeProgressive.LinearGradient(
                            startIntensity = intensity,
                            endIntensity = intensity,
                            preferPerformance = true
                        )
                    }),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                Text(
                    text = "저장공간 권한이 꺼져있습니다.\n\n" +
                            "사진 업로드를 위해서는 [권한] 설정에서 사진 및 동영상 권한을 허용해야 합니다.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(24.dp)
                )

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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "닫기",
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = Color.Red,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onFailure() }
                    )

                    Text(
                        text = "설정으로 이동",
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onConfirm() }
                    )
                }
            }
        }
    }
}