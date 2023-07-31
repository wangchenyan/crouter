package me.wcy.router;

import java.util.HashSet;
import java.util.Set;

import me.wcy.router.annotation.RouteInfo;
import me.wcy.router.annotation.RouteLoader;

/**
 * 路由收集器
 */
class RouteSet {
    private static final Set<RouteInfo> sRouteSet = new HashSet<>();

    public static Set<RouteInfo> get() {
        return sRouteSet;
    }

    public static void register(RouteLoader routeLoader) {
        routeLoader.loadRoute(sRouteSet);
    }
}
