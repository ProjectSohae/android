package com.sohae.presentation.jobreview

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class JobReviewViewModel: ViewModel() {

    val reviewDetails = MutableStateFlow<Int>(0)

}