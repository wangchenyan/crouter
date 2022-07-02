package me.wcy.router.interceptor

import android.content.Context
import android.content.Intent
import android.net.Uri
import me.wcy.router.CRouter
import me.wcy.router.FragmentFinder
import me.wcy.router.Interceptor
import me.wcy.router.Response
import me.wcy.router.RouterUtils
import me.wcy.router.annotation.Route

/**
 * URI 拦截器
 */
class UriInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val uri = request.uri()
        uri?.let {
            CRouter.getRouteSet().find { route: Route ->
                RouterUtils.match(route, uri)
            }?.let { route: Route ->
                val extras = Intent()
                for (key in uri.queryParameterNames) {
                    extras.putExtra(key, uri.getQueryParameter(key))
                }
                val intent = getIntent(request.context(), uri, route.target(), extras)
                return Response.Builder()
                    .context(request.context())
                    .request(request)
                    .intent(intent)
                    .needLogin(request.needLogin() || route.needLogin())
                    .build()
            }
        }
        return chain.proceed(request)
    }

    private fun getIntent(context: Context, uri: Uri, target: Class<*>, extras: Intent): Intent {
        val intent: Intent
        if (FragmentFinder.isAnyFragment(target)
            && CRouter.getRouterClient().fragmentContainerIntentProvider() != null
        ) {
            intent = CRouter.getRouterClient().fragmentContainerIntentProvider()!!.invoke(context)
            intent.putExtras(extras)
            intent.putExtra(CRouter.CROUTER_KEY_FRAGMENT_URI, uri)
        } else {
            intent = Intent(context, target)
            intent.putExtras(extras)
        }
        return intent
    }
}
