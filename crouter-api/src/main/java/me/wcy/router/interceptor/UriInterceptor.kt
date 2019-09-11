package me.wcy.router.interceptor

import android.content.Intent
import me.wcy.router.CRouter
import me.wcy.router.Interceptor
import me.wcy.router.Response
import me.wcy.router.RouterUtils

/**
 * URI 拦截器
 */
class UriInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val uri = request.uri()
        uri?.let {
            val routerSet = CRouter.routerSet()
            for (router in routerSet) {
                if (RouterUtils.match(router, uri)) {
                    val context = request.context()
                    val intent = Intent(context, router.target())
                    val extras = Intent()
                    for (key in uri.queryParameterNames) {
                        extras.putExtra(key, uri.getQueryParameter(key))
                    }
                    intent.putExtras(extras)

                    return Response.Builder()
                            .context(context)
                            .request(request)
                            .intent(intent)
                            .needLogin(request.needLogin() || router.needLogin())
                            .build()
                }
            }
        }
        return chain.proceed(request)
    }
}
