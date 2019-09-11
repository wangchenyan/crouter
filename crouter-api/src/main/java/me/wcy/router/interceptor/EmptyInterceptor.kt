package me.wcy.router.interceptor

import me.wcy.router.Interceptor
import me.wcy.router.Response

/**
 * 空拦截器，仅构造一个路由响应
 */
class EmptyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val context = request.context()
        return Response.Builder()
                .context(context)
                .request(request)
                .build()
    }
}
