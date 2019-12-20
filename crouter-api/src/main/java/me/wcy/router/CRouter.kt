package me.wcy.router

import android.content.Context
import android.content.Intent
import me.wcy.router.annotation.Route
import me.wcy.router.annotation.Router

/**
 * 路由入口
 */
object CRouter {
    internal const val TAG = "CRouter"

    private var routerClient = RouterClient()
    private val routerStarter = RealRouterStarter()
    private val resultManager = ResultManager()
    private var loginProvider: LoginProvider? = null

    /**
     * 添加拦截器
     */
    fun addInterceptor(interceptor: Interceptor) {
        routerClient =
            getRouterClient().newBuilder().addInterceptor(interceptor).build()
    }

    /**
     * 设置登录提供者。设置后 [Router.needLogin] 才能生效
     */
    fun setLoginProvider(loginProvider: LoginProvider) {
        this.loginProvider = loginProvider
    }

    /**
     * 开始路由
     */
    fun with(context: Context?): Request.Builder {
        if (context == null) {
            throw IllegalStateException("context == null")
        }
        return Request.Builder().context(context)
    }

    /**
     * 通知 Activity 结果，一般在 Activity 基类中设置
     *
     * 示例代码
     *
     * ```
     * override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
     *     val handle = CRouter.onActivityResult(requestCode, resultCode, data)
     *     if (!handle) {
     *         super.onActivityResult(requestCode, resultCode, data)
     *     }
     * }
     * ```
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return getResultManager().onActivityResult(requestCode, resultCode, data)
    }

    internal fun getRouterClient(): RouterClient {
        return routerClient
    }

    internal fun getRouterStarter(): RouterStarter {
        return routerStarter
    }

    internal fun getResultManager(): ResultManager {
        return resultManager
    }

    internal fun getRouterSet(): Set<Route> {
        return RouterSet.get()
    }

    internal fun getLoginProvider(): LoginProvider? {
        return loginProvider
    }
}
