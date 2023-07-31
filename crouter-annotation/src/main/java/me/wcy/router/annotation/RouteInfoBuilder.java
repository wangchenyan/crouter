package me.wcy.router.annotation;

import org.jetbrains.annotations.NotNull;

/**
 * 路由信息构建器
 */
public class RouteInfoBuilder {

    public static RouteInfo buildRouteInfo(String url, Class target, boolean needLogin) {
        return new RouteInfo() {
            @NotNull
            @Override
            public String url() {
                return url;
            }

            @NotNull
            @Override
            public Class target() {
                return target;
            }

            @Override
            public boolean needLogin() {
                return needLogin;
            }
        };
    }
}