package me.wcy.router.annotation;

import java.util.Set;

/**
 * 路由加载器
 */
public interface RouterLoader {
    void loadRouter(Set<Route> routeSet);
}