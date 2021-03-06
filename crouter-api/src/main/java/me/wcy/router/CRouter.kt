package me.wcy.router

import android.content.Context
import me.wcy.router.annotation.Route

/**
 * 路由入口
 */
object CRouter {
    internal const val TAG = "CRouter"
    private var routerClient: RouterClient? = null
    private lateinit var context: Context

    /**
     * 设置路由客户端，不设置则使用默认客户端
     */
    fun setRouterClient(routerClient: RouterClient) {
        this.routerClient = routerClient
    }

    /**
     * 开始路由
     *
     * @param context 上下文，如果需要获取页面结果则需要传入 Activity
     */
    fun with(context: Context? = null): Request.Builder {
        return Request.Builder().context(context ?: this.context)
    }

    internal fun setContext(context: Context) {
        this.context = context
    }

    internal fun getRouterClient(): RouterClient {
        if (routerClient == null) {
            routerClient = RouterClient()
        }
        return routerClient!!
    }

    internal fun getRouterSet(): Set<Route> {
        return RouterSet.get()
    }
}
