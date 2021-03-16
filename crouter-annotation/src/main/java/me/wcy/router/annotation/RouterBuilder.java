package me.wcy.router.annotation;

import org.jetbrains.annotations.NotNull;

/**
 * 路由信息构建器
 */
public class RouterBuilder {

    public static Route buildRouter(String path, Class target, boolean needLogin) {
        return new Route() {
            @NotNull
            @Override
            public String path() {
                return path;
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