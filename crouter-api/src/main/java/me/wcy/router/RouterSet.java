package me.wcy.router;

import java.util.HashSet;
import java.util.Set;

import me.wcy.router.annotation.Route;
import me.wcy.router.annotation.RouterLoader;

/**
 * 路由收集器
 */
class RouterSet {
    private static final Set<Route> sRouterSet = new HashSet<>();

    public static Set<Route> get() {
        return sRouterSet;
    }

    public static void register(RouterLoader routerLoader) {
        routerLoader.loadRouter(sRouterSet);
    }
}
