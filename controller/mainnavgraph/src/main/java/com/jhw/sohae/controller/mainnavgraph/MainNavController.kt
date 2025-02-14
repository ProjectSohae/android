package com.jhw.sohae.controller.mainnavgraph

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object MainNavController {
    private var _route = MutableStateFlow("")
    val route = _route.asStateFlow()

    private var _param = MutableStateFlow(Pair("", -1))
    val param = _param.asStateFlow()

    private var _backPressed = MutableStateFlow(false)
    val backPressed = _backPressed.asStateFlow()

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
}