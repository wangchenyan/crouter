package me.wcy.router.annotation

/**
 * 标记路由信息，仅支持 Activity
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Route(
    /**
     * URL path，可以为 "" 或者以 "/" 开头，例如 "/example\\.html"，支持正则表达式，注意需要转义
     */
    val value: String,
    /**
     * URL scheme，不包含 "://"，例如 "http"，支持正则表达式，注意需要转义
     */
    val scheme: String = "",
    /**
     * URL host，不包含 "/"，例如 "www\\.google\\.com"，支持正则表达式，注意需要转义
     */
    val host: String = "",
    /**
     * 是否需要登录，默认不需要
     *
     * 需要调用 [RouterClient.Builder().loginProvider()] 才能生效
     */
    val needLogin: Boolean = false
)
