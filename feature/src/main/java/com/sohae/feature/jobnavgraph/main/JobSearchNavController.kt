package com.sohae.feature.jobnavgraph.main

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object JobSearchNavController {
    private var _route = MutableStateFlow("")
    val route = _route.asStateFlow()

    fun navigate(inputRoute: String) {
        _route.value = inputRoute
    }
}