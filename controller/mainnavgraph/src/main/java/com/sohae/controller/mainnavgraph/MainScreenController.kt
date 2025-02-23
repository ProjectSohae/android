package com.sohae.controller.mainnavgraph

import dev.chrisbanes.haze.HazeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MainScreenController {
    private var _isDeactive = MutableStateFlow(false)
    val isDeactive : StateFlow<Boolean> = _isDeactive.asStateFlow()

    val hazeState = HazeState()

    fun deactive() {
        _isDeactive.value = true
    }

    fun active() {
        _isDeactive.value = false
    }
}