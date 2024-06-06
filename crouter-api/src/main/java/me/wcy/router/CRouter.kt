package me.wcy.router

import android.app.Application
import android.content.Context
import me.wcy.router.annotation.CRouterConsts
import me.wcy.router.annotation.RouteMeta

/**
 * 路由入口
 */
object CRouter {
    const val CROUTER_KEY_FRAGMENT_URI = "crouter_key_fragment_uri"
    internal const val TAG = "CRouter"

    private var routerClient: RouterClient? = null
    private lateinit var application: Application

    internal val routes: MutableSet<RouteMeta> = mutableSetOf()

    init {
        val routeRegisterer = Class.forName(CRouterConsts.FINAL_REGISTERER_CLASS_NAME)
            .newInstance() as IRouteRegisterer
        routeRegisterer.registerModuleRoutes()
    }

    /**
     * 设置路由客户端，不设置则使用默认客户端
     */
    fun setRouterClient(client: RouterClient) {
        this.routerClient = client
    }

    /**
     * 开始路由
     *
     * @param context 上下文，如果需要获取页面结果则需要传入 Activity
     */
    fun with(context: Context? = null): Request.Builder {
        return Request.Builder().context(context ?: this.application)
    }

    fun register(routeMeta: RouteMeta) {
        routes.add(routeMeta)
    }

    internal fun setContext(application: Application) {
        this.application = application
    }

    internal fun getRouterClient(): RouterClient {
        if (routerClient == null) {
            routerClient = RouterClient()
        }
        return routerClient!!
    }
}
