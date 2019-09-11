package me.wcy.router.interceptor

import me.wcy.router.Interceptor
import me.wcy.router.Response

/**
 * 意图拦截器
 */
class IntentInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val intent = request.intent()
        if (intent != null && (intent.component != null || intent.action != null)) {
            val context = request.context()
            return Response.Builder()
                    .context(context)
                    .request(request)
                    .intent(request.intent()!!)
                    .needLogin(request.needLogin())
                    .build()
        }
        return chain.proceed(request)
    }
}
