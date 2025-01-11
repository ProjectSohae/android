package com.example.gongik.view.composables.main

import dev.chrisbanes.haze.HazeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class MainNavGraphBarItems {
    HOMENAV,
    JOBREVIEW,
    WRITEJOBREVIEW,
    WRITEPOST,
    FINDPOST,
    FINDJOB,
    SETTING
}

object MainNavGraphViewModel {
    private var _route = MutableStateFlow("")
    val route : StateFlow<String> = _route.asStateFlow()

    private var _backPressed = MutableStateFlow(false)
    val backPressed = _backPressed.asStateFlow()

    private var _isDeactive = MutableStateFlow(false)
    val isDeactive : StateFlow<Boolean> = _isDeactive.asStateFlow()

    val hazeState = HazeState()

    fun navigate(inputRoute : String) {
        _route.value = inputRoute
    }

    fun popBack() {
        _backPressed.value = true
    }

    fun finishPopBack() {
        _backPressed.value = false
    }

    fun deactive() {
        _isDeactive.value = true
    }

    fun active() {
        _isDeactive.value = false
    }
}