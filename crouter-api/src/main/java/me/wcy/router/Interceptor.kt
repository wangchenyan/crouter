package me.wcy.router

/**
 * 路由拦截器
 */
interface Interceptor {
    /**
     * 拦截请求
     */
    fun intercept(chain: Chain): Response

    /**
     * 路由链
     */
    interface Chain {
        /**
         * 获取路由请求
         */
        fun request(): Request

        /**
         * 继续请求
         */
        fun proceed(request: Request): Response

        fun call(): Call
    }
}
