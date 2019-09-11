package me.wcy.router.interceptor

import android.app.Activity
import android.content.Intent
import me.wcy.router.Interceptor
import me.wcy.router.Response

/**
 * 类型拦截器
 */
class TargetInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val target = request.target()
        if (target != null && Activity::class.java.isAssignableFrom(target)) {
            val context = request.context()
            val intent = Intent(context, target)
            return Response.Builder()
                    .context(context)
                    .request(request)
                    .intent(intent)
                    .needLogin(request.needLogin())
                    .build()
        }
        return chain.proceed(request)
    }
}
