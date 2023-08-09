package me.wcy.router.annotation

/**
 * 路由加载器
 */
interface RouteLoader {
    fun loadRoute(routeSet: MutableSet<RouteInfo>)
}