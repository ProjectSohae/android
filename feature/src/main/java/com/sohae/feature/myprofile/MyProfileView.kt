package com.sohae.feature.myprofile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.sohae.common.resource.R
import com.sohae.common.ui.custom.composable.ProfileImage
import com.sohae.common.ui.custom.dialog.TypingTextDialog
import com.sohae.common.ui.custom.snackbar.SnackBarBehindTarget
import com.sohae.common.ui.custom.snackbar.SnackBarController
import com.sohae.controller.mainnavgraph.MainNavGraphViewController
import com.sohae.controller.mainnavgraph.MainScreenController
import com.sohae.domain.myinformation.entity.MyAccountEntity
import com.sohae.feature.myprofile.signin.SignInView

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun MyProfileView(
    myProfileViewModel: MyProfileViewModel
) {
    val brightTertiary = Color(
        ColorUtils.blendARGB(
            MaterialTheme.colorScheme.tertiary.toArgb(),
            MaterialTheme.colorScheme.onPrimary.toArgb(),
            0.75f
        )
    )

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
                .background(brightTertiary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MyProfileHeaderView()

            MyProfileBodyView(myProfileViewModel)
        }
    }
}

@Composable
private fun MyProfileHeaderView(
) {
    val mainNavController = MainNavGraphViewController.mainNavController
    val tertiary = MaterialTheme.colorScheme.tertiary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
            .drawBehind {
                drawLine(
                    color = tertiary,
                    start = Offset(0f, this.size.height),
                    end = Offset(this.size.width, this.size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .padding(start = 12.dp, end = 24.dp, top = 8.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable { mainNavController.popBackStack() },
            contentDescription = null
        )

        Text(
            text = "내 프로필",
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun MyProfileBodyView(
    myProfileViewModel: MyProfileViewModel
) {
    val tertiary = MaterialTheme.colorScheme.tertiary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val itemModifier = Modifier
        .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
        .fillMaxWidth()
        .drawBehind {
            drawRoundRect(
                color = onPrimary,
                cornerRadius = CornerRadius(50f, 50f)
            )
        }
    val myAccount = myProfileViewModel.myAccount.collectAsState().value
    var isReadyInfo by remember { mutableStateOf(false) }
    var showDialog by remember { mutableIntStateOf(-1) }

    LaunchedEffect(myAccount) {

        if (myAccount != null) {
            isReadyInfo = true
        }
    }

    when (showDialog) {
        // 닉네임 설정
        0 -> {
            if (myAccount != null) {
                TypingTextDialog(
                    hazeState = MainScreenController.hazeState,
                    intensity = 0.75f,
                    title = "닉네임 설정",
                    content = "설정 하고자 하는 닉네임을 입력해 주세요.",
                    inputFormatsList = listOf(Pair("닉네임 입력(10자 이내)", "")),
                    initialValuesList = listOf(myAccount!!.username),
                    limitLength = 10,
                    isIntegerList = listOf(false),
                    keyboardOptionsList = listOf(
                        KeyboardOptions(keyboardType = KeyboardType.Ascii)
                    ),
                    onDismissRequest = { showDialog = -1 },
                    onConfirmation = {
                        myProfileViewModel.updateMyUsername(
                            MyAccountEntity(
                                id = myAccount.id,
                                username = it,
                                emailAddress = myAccount.emailAddress
                            )
                        )
                        showDialog = -1
                    }
                )
            } else {
                SnackBarController.show("로그인이 필요합니다.", SnackBarBehindTarget.VIEW)
            }
        }
        // 로그아웃
        1 -> {
            myProfileViewModel.signOut()

            showDialog = -1
        }
        // 로그인
        2 -> {
            SignInView(
                onDismissRequest = { showDialog = -1 },
                onConfirm = {


                    showDialog = -1
                }
            )
        }
        3 -> {


            showDialog = -1
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ProfileImage(
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .size(120.dp),
                innerPadding = PaddingValues(top = 8.dp)
            )
        }

        item {
            Column(
                modifier = itemModifier.padding(16.dp)
            ) {
                Text(
                    text = "내 정보",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // 닉네임
                Row(
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .fillMaxWidth()
                        .clickable { showDialog = 0 },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "닉네임",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = myAccount?.username ?: "닉네임 없음",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // 이메일
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "이메일",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = myAccount?.emailAddress ?: "로그인이 필요합니다.",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }

        item {
            Column(
                modifier = itemModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (myAccount != null) {
                    Text(
                        text = "로그아웃",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawBehind {
                                drawLine(
                                    color = tertiary,
                                    start = Offset(this.size.width * 0.1f, this.size.height),
                                    end = Offset(this.size.width * 0.9f, this.size.height),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }
                            .clickable { showDialog = 1 }
                            .padding(vertical = 16.dp)
                    )
                } else {
                    Text(
                        text = "로그인",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawBehind {
                                drawLine(
                                    color = tertiary,
                                    start = Offset(this.size.width * 0.1f, this.size.height),
                                    end = Offset(this.size.width * 0.9f, this.size.height),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }
                            .clickable { showDialog = 2 }
                            .padding(vertical = 16.dp)
                    )
                }

                Text(
                    text = "회원 탈퇴",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDialog = 3 }
                        .padding(vertical = 16.dp)
                )
            }
        }
    }
}