package com.example.gongik.view.main

import dev.chrisbanes.haze.HazeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class MainNavGraphItems {
    HOMENAV,

    WRITEPOST,
    POST,
    SEARCHPOST,

    JOBREVIEW,
    WRITEJOBREVIEW,
    SEARCHJOB,

    MYPROFILE,
    SETTINGOPTIONS
}

object MainNavGraphViewModel {
    private var _route = MutableStateFlow("")
    val route = _route.asStateFlow()

    private var _param = MutableStateFlow(Pair("", -1))
    val param = _param.asStateFlow()

    private var _backPressed = MutableStateFlow(false)
    val backPressed = _backPressed.asStateFlow()

    private var _isDeactive = MutableStateFlow(false)
    val isDeactive : StateFlow<Boolean> = _isDeactive.asStateFlow()

    val hazeState = HazeState()

    fun navigate(inputRoute : String) {
        _route.value = inputRoute
    }

    fun setParam(key: String, value: Int) {
        _param.value = Pair(key, value)
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