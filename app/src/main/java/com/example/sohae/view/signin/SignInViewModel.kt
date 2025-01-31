package com.example.sohae.view.signin

import android.content.Context
import android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sohae.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch

class SignInViewModel: ViewModel() {

    val TAG = "SignIn"

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun googleSignIn(
        currentContext: Context,
        callback: () -> Unit
    ) {
        val firstGoogleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            // 이전에 동일한 계정으로 인증한 적 있는지 확인
            // true: 사용한 적 있는 계정들 중 하나를 선택하는 창 표시
            // false: 계정 생성 창 표시
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            // 이전에 로그인 한 계정으로 자동 로그인 여부
            .setAutoSelectEnabled(true)
            //.setNonce()
            .build()

        val secondGoogleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            //.setNonce()
            .build()

        viewModelScope.launch {
            try {
                val result = CredentialManager
                    .create(currentContext).getCredential(
                        request = GetCredentialRequest.Builder()
                            .addCredentialOption(firstGoogleIdOption)
                            .build(),
                        context = currentContext
                    )
                handleGoogleSignIn(result)
            } catch (e: GetCredentialException) {

                // 앱에 로그인 했던 계정이 없을 시, 새 계정으로 앱 가입
                if (e.type == TYPE_NO_CREDENTIAL) {
                    val result = CredentialManager
                        .create(currentContext).getCredential(
                            request = GetCredentialRequest.Builder()
                                .addCredentialOption(secondGoogleIdOption)
                                .build(),
                            context = currentContext
                        )
                    handleGoogleSignIn(result)
                }
            }

            callback()
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun handleGoogleSignIn(
        result: GetCredentialResponse
    ) {
        // 반환 받은 인증값
        val credential = result.credential

        when (credential) {

            // 패스키 인증값
            is PublicKeyCredential -> {
                val responseJson = credential.authenticationResponseJson
            }

            // 아이디, 비밀번호 인증값
            is PasswordCredential -> {
                // Send ID and password to your server to validate and authenticate.
                val username = credential.id
                val password = credential.password
            }

            // 구글 id 토큰 인증값
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {

                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // 유효하지 않은 custom 인증 종류
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // 유효하지 않은 인증 종류
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    fun kakaoSignIn(
        currentContext: Context,
        callback: () -> Unit
    ) {
        viewModelScope.launch {

            // 카카오톡 설치 여부 확인
            if (
                UserApiClient.instance.isKakaoTalkLoginAvailable(currentContext)
            ) {
                // 카카오톡 앱을 호출하여 로그인
                UserApiClient.instance.loginWithKakaoTalk(currentContext) { token, error ->

                    if (error != null) {
                        Log.e(TAG, "로그인 실패", error)
                    }
                    else if (token != null) {
                        Log.i(TAG, "로그인 성공 ${token.accessToken}")
                    }

                    callback()
                }
            } else {
                // 카카오톡 홈페이지를 호출하여 로그인
                UserApiClient.instance.loginWithKakaoAccount(currentContext) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "로그인 실패", error)
                    }
                    else if (token != null) {
                        Log.i(TAG, "로그인 성공 ${token.accessToken}")
                    }

                    callback()
                }
            }
        }
    }
}