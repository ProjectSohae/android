package com.sohae.feature.myprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohae.domain.myinformation.entity.MyAccountEntity
import com.sohae.domain.myinformation.usecase.MyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val myInfoUseCase: MyInfoUseCase
): ViewModel() {

    val myAccount = myInfoUseCase.getMyAccount().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

    fun signOut() {
        myInfoUseCase.deleteMyAccount()
        myInfoUseCase.deleteMyToken()
    }

    fun updateMyUsername(input: MyAccountEntity) {
        myInfoUseCase.updateMyAccount(input)
    }
}