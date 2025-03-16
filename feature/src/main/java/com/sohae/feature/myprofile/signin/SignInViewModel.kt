package com.sohae.feature.myprofile.signin

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import com.kakao.sdk.user.UserApiClient
import com.sohae.domain.myinformation.entity.MyTokenEntity
import com.sohae.domain.myinformation.usecase.MyInfoUseCase
import com.sohae.domain.profile.usecase.ProfileUseCase
import com.sohae.domain.session.entity.AuthTokenEntity
import com.sohae.domain.session.type.AuthType
import com.sohae.domain.session.usecase.SessionUseCase
import com.sohae.feature.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val sessionUseCase: SessionUseCase,
    private val myInfoUseCase: MyInfoUseCase,
    private val profileUseCase: ProfileUseCase
): ViewModel() {

    private val tag = "sohae_signIn"

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun getSocialAccessToken(
        type: AuthType,
        context: Context,
        reCheckForGoogleSignInView: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
        callback: (Boolean) -> Unit
    ) {
        val success = { authType: AuthType, socialAccessToken: String ->

            getMyToken(authType, socialAccessToken) { isSucceed ->
                callback(isSucceed)
            }
        }
        val failure = { callback(false) }

        viewModelScope.launch {

            when (type) {
                AuthType.KAKAO -> {
                    kakaoSignIn(context) { getToken, isSucceed ->

                        if (isSucceed) {
                            success(AuthType.KAKAO, getToken)
                        } else {
                            failure()
                        }
                    }
                }
                AuthType.GOOGLE -> {
                    googleSignIn(
                        context,
                        reCheckForGoogleSignInView
                    ) { getToken, isSucceed ->

                        if (isSucceed) {
                            success(AuthType.GOOGLE, getToken)
                        } else {
                            failure()
                        }
                    }
                }
                else -> {
                    failure()
                }
            }
        }
    }

    fun getMyToken(
        authType: AuthType,
        socialToken: String,
        callback: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            sessionUseCase.getAuthToken(authType, socialToken) { authTokenEntity ->

                if (authTokenEntity != null) {
                    insertNewMyToken(authTokenEntity)
                    insertNewMyAccount(authTokenEntity.accessToken)
                    callback(true)
                } else {
                    callback(false)
                }
            }
        }
    }

    private fun insertNewMyToken(
        authTokenEntity: AuthTokenEntity
    ) {
        myInfoUseCase.updateMyToken(
            MyTokenEntity(
                0,
                authTokenEntity.accessToken,
                authTokenEntity.refreshToken
            )
        )
    }

    private fun insertNewMyAccount(
        accessToken: String
    ) {
        profileUseCase.getMyProfile(accessToken) { myProfileEntity ->
            myProfileEntity?.let {
                myInfoUseCase.updateMyAccount(myProfileEntity)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun googleSignIn(
        currentContext: Context,
        reCheckForGoogleSignInView: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
        callBack: (String, Boolean) -> Unit
    ) {
        val success = { socialAccessToken: String ->
            callBack(socialAccessToken, true)
        }
        val failure = {
            callBack("", false)
        }

        val requestedScopes = listOf(
            Scope(Scopes.PROFILE),
            Scope("https://www.googleapis.com/auth/userinfo.email")
        )

        val authorizationRequest = AuthorizationRequest.builder()
            .setRequestedScopes(requestedScopes)
            .requestOfflineAccess(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .build()

        Identity.getAuthorizationClient(currentContext)
            .authorize(authorizationRequest)
            .addOnSuccessListener { authorizationResult ->

                if (authorizationResult.hasResolution()) {

                    authorizationResult.pendingIntent?.intentSender?.let { intentSender ->
                        reCheckForGoogleSignInView.launch(IntentSenderRequest.Builder(intentSender).build())
                    } ?: {
                        failure()
                    }
                } else {

                    authorizationResult.accessToken?.let {
                        success(it)
                    } ?: {
                        failure()
                    }
                }
            }
            .addOnFailureListener {

            }
    }

    private fun kakaoSignIn(
        currentContext: Context,
        callBack: (String, Boolean) -> Unit
    ) {
        val success = { socialToken: String ->
            callBack(socialToken, true)
        }
        val failure = {
            callBack("", false)
        }

        // 카카오톡 설치 여부 확인
        if (
            UserApiClient.instance.isKakaoTalkLoginAvailable(currentContext)
        ) {
            // 카카오톡 앱을 호출하여 로그인
            UserApiClient.instance.loginWithKakaoTalk(currentContext) { token, error ->

                if (error != null) {
                    Log.e(tag, "로그인 실패", error)
                    failure()
                }
                else if (token != null) {
                    Log.i(tag, "로그인 성공 ${token.accessToken}")
                    success(token.accessToken)
                }
            }
        } else {
            // 카카오톡 홈페이지를 호출하여 로그인
            UserApiClient.instance.loginWithKakaoAccount(currentContext) { token, error ->
                if (error != null) {
                    Log.e(tag, "로그인 실패", error)
                    failure()
                }
                else if (token != null) {
                    Log.i(tag, "로그인 성공 ${token.accessToken}")
                    success(token.accessToken)
                }
            }
        }
    }


}