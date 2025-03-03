package com.sohae.presentation.selectimage

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.sohae.common.resource.R
import com.sohae.common.ui.custom.snackbar.SnackBarBehindTarget
import com.sohae.common.ui.custom.snackbar.SnackBarController
import com.sohae.controller.mainnavgraph.MainNavGraphViewController
import com.sohae.controller.mainnavgraph.MainScreenController
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
    var imageUris by rememberSaveable { mutableStateOf<List<Uri>>(emptyList()) }

    LaunchedEffect(Unit) {

        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            isGranted = true
        } else {
            permissionLauncher.launch(permission)
        }
    }

    LaunchedEffect(isGranted) {

        if (isGranted) {
            imageUris = selectImageViewModel.getAllImages(context)
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
                            data = Uri.parse("package:${context.packageName}")
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
    imageUris: List<Uri>,
    selectImageViewModel: SelectImageViewModel
) {
    var itemHeight by remember { mutableStateOf(0) }
    val rowSize: Int = (imageUris.size) / 4 +
            (imageUris.size % 4 > 0).let { if (it) { 1 } else { 0 } }
    val selectedImageList = selectImageViewModel.selectedImagesList.collectAsState().value

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            for (rowIdx: Int in 0..<rowSize) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (columnIdx:Int in 0..3) {
                        val itemIdx: Int = rowIdx * 4 + columnIdx

                        if (itemIdx < imageUris.size) {
                            SelectImageItem(
                                imageUri = imageUris[itemIdx],
                                isSelected = selectedImageList.contains(imageUris[itemIdx]),
                                selectedIdx = selectedImageList.indexOf(imageUris[itemIdx]) + 1,
                                modifier = Modifier
                                    .fillParentMaxWidth(0.25f)
                                    .onGloballyPositioned {
                                        itemHeight = it.size.width
                                    }
                                    .height(
                                        with(LocalDensity.current) {
                                            itemHeight.toDp()
                                        }
                                    ),
                                onClick = {
                                    selectImageViewModel.selectImage(imageUris[itemIdx]).let {
                                        if (it.isNotBlank()) {
                                            SnackBarController.show(it, SnackBarBehindTarget.VIEW)
                                        }
                                    }
                                }
                            )
                        } else {
                            break
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectImageItem(
    imageUri: Uri,
    isSelected: Boolean,
    selectedIdx: Int,
    modifier: Modifier,
    onClick: () -> Unit
) {
    var idxItemWidth by remember { mutableIntStateOf(0) }
    val roundRect: (Float, Float) -> RoundRect = { width, height ->
        RoundRect(
            top = height * 0.15f,
            bottom = height * 0.85f,
            left = width * 0.15f,
            right = width * 0.85f,
            cornerRadius = CornerRadius(100f, 100f)
        )
    }
    val imageAlpha = if (isSelected) { 0.4f } else { 0f }
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onPrimary
    }
    val primary = MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .padding(2.dp)
            .border(
                width = 2.dp,
                color = borderColor
            )
            .clickable(
                interactionSource = null,
                indication = null
            ) { onClick() }
    ) {
        AsyncImage(
            model = imageUri,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .alpha(imageAlpha),
            color = MaterialTheme.colorScheme.tertiary
        ) {}

        Text(
            text = selectedIdx.let { if (it < 1) { "" } else { it } }.toString(),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .onGloballyPositioned { idxItemWidth = it.size.height }
                .width(with(LocalDensity.current) { idxItemWidth.toDp() })
                .drawWithContent {

                    if (isSelected) {
                        drawRoundRect(
                            color = primary,
                            topLeft = Offset(this.size.width * 0.05f, this.size.height * 0.05f),
                            size = Size(this.size.width * 0.9f, this.size.height * 0.9f),
                            cornerRadius = CornerRadius(100f, 100f)
                        )
                    }

                    drawContent()

                    if (!isSelected) {
                        drawOutline(
                            outline = Outline.Rounded(
                                roundRect = roundRect(this.size.width, this.size.height)
                            ),
                            alpha = 0.2f,
                            color = Color.Black,
                            style = Stroke(width = 3.dp.toPx())
                        )
                        drawOutline(
                            outline = Outline.Rounded(
                                roundRect = roundRect(this.size.width, this.size.height)
                            ),
                            color = borderColor,
                            style = Stroke(width = (1.5f).dp.toPx())
                        )
                    }
                }
        )
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