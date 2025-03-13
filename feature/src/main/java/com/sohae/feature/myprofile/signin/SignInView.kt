package com.sohae.feature.myprofile.signin

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.identity.Identity
import com.sohae.common.resource.R
import com.sohae.common.ui.custom.snackbar.SnackBarBehindTarget
import com.sohae.common.ui.custom.snackbar.SnackBarController
import com.sohae.common.ui.custom.snackbar.SnackbarView
import com.sohae.controller.mainnavgraph.MainScreenController
import com.sohae.domain.signin.type.AuthType
import dev.chrisbanes.haze.HazeEffectScope
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun SignInView(
    intensity: Float = 0.85f,
    signInViewModel: SignInViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    val currentContext = LocalContext.current
    var selectedSignInType by remember { mutableStateOf<AuthType?>(null) }
    val signInSucceed = {
        SnackBarController.show(
            "로그인 성공.",
            SnackBarBehindTarget.VIEW
        )
        onConfirm()
    }
    val signInFailed = {
        SnackBarController.show(
            "로그인 실패. 다시 시도해 주세요.",
            SnackBarBehindTarget.VIEW
        )
        onDismissRequest()
    }
    val reCheckCallbackLogic = { authType: AuthType, socialAccessToken: String ->

        if (socialAccessToken.isBlank()) {
            signInFailed()
        } else {
            signInViewModel.getMyToken(authType, socialAccessToken) { isSucceed ->

                if (isSucceed) {
                    signInSucceed()
                } else {
                    signInFailed()
                }
            }
        }
    }
    val reCheckForGoogleSignInView = reCheckForSignInView(
        AuthType.GOOGLE,
        currentContext,
        reCheckCallbackLogic
    )

    LaunchedEffect(selectedSignInType) {

        if (selectedSignInType != null) {

            signInViewModel.getSocialAccessToken(
                selectedSignInType!!,
                currentContext,
                reCheckForGoogleSignInView
            ) { isSucceed ->

                if (isSucceed) {
                    signInSucceed()
                } else {
                    signInFailed()
                }
            }
        }
    }


    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(10)
                )
                .clip(RoundedCornerShape(10))
                .hazeEffect(
                    state = MainScreenController.hazeState,
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
                    })
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "로그인",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // 카카오 로그인
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    .width(260.dp)
                    .clip(RoundedCornerShape(25))
                    .background(Color(0xFFFEE500))
                    .clickable {

                        if (selectedSignInType == null) {
                            selectedSignInType = AuthType.KAKAO
                        }
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    painter = painterResource(id = R.drawable.kakao_logo),
                    modifier = Modifier.size(18.dp),
                    contentDescription = null
                )

                Text(
                    text = "카카오 로그인",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.85f)
                )
            }

            // 구글 로그인
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .width(260.dp)
                    .clip(RoundedCornerShape(25))
                    .background(Color(0xFFF2F2F2))
                    .clickable {

                        if (selectedSignInType == null) {
                            selectedSignInType = AuthType.GOOGLE
                        }
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_logo),
                    modifier = Modifier.size(18.dp),
                    contentDescription = null
                )

                Text(
                    text = "구글 로그인",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.54f)
                )
            }
        }
    }
}

@Composable
private fun reCheckForSignInView(
    authType: AuthType,
    context: Context,
    callback: (AuthType, String) -> Unit
) = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->

        var authorizationClient: AuthorizationResult? = null

        if (activityResult.resultCode == Activity.RESULT_OK) {
            authorizationClient = Identity.getAuthorizationClient(context)
                .getAuthorizationResultFromIntent(activityResult.data)
        }

        if (
            authorizationClient == null
            || authorizationClient.accessToken == null
        ) {
            callback(authType, "")
        } else {
            callback(authType, authorizationClient.accessToken!!)
        }
    }