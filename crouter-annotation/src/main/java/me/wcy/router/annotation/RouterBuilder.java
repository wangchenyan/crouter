package me.wcy.router.annotation;

import org.jetbrains.annotations.NotNull;

/**
 * 路由信息构建器
 */
public class RouterBuilder {

    public static Route buildRouter(String url, Class target, boolean needLogin) {
        return new Route() {
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