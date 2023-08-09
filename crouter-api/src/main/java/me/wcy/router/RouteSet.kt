package me.wcy.router

import me.wcy.router.annotation.RouteInfo
import me.wcy.router.annotation.RouteLoader

/**
 * 路由收集器
 */
internal class RouteSet {
    val routeSet: MutableSet<RouteInfo> = mutableSetOf()

    init {
        init()
    }

    private fun init() {
        // Inject code here
    }

    fun register(routeLoader: RouteLoader) {
        routeLoader.loadRoute(routeSet)
    }
}