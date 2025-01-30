package com.example.sohae.view.login

import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import com.example.sohae.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption

class LoginViewModel: ViewModel() {

    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        // 이전에 동일한 계정으로 인증한 적 있는지 확인
        // true: 없으면 사용가능한 계정 선택하는 창 표시
        // false: 없으면 계정 생성 창 표시
        .setFilterByAuthorizedAccounts(true)
        .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
        // 이전에 로그인 한 계정으로 자동 로그인 여부
        .setAutoSelectEnabled(true)
        //.setNonce()
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

}