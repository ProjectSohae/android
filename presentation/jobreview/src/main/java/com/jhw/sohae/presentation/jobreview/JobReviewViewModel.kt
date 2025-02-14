package com.jhw.sohae.presentation.jobreview

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class JobReviewViewModel: ViewModel() {

    val reviewDetails = MutableStateFlow<Int>(0)

}