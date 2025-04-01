package com.sohae.feature.myprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohae.domain.myinformation.entity.MyAccountEntity
import com.sohae.domain.myinformation.usecase.MyInfoUseCase
import com.sohae.domain.session.usecase.SessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val myInfoUseCase: MyInfoUseCase,
    private val sessionUseCase: SessionUseCase
): ViewModel() {

    val myAccount = myInfoUseCase.getMyAccount().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

    private var _isWaitingResponse = MutableStateFlow(false)
    val isWaitingResponse = _isWaitingResponse.asStateFlow()

    fun setIsWaitingResponse(input: Boolean) {
        _isWaitingResponse.value = input
    }

    fun signOut(
        callback: (Boolean) -> Unit
    ) {
        setIsWaitingResponse(true)

        sessionUseCase.signOut { isSucceed ->

            if (isSucceed) {
                myInfoUseCase.deleteMyAccount()
                myInfoUseCase.deleteMyToken()
                callback(true)
            } else {
                callback(false)
            }
        }
    }

    fun updateMyUsername(input: MyAccountEntity) {
        myInfoUseCase.updateMyAccount(input)
    }
}