package me.wcy.router

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import me.wcy.router.annotation.Route
import me.wcy.router.annotation.Router

/**
 * 路由入口
 */
class CRouter private constructor() {
    private var routerClient = RouterClient()
    private val routerStarter = RealRouterStarter()
    private val resultManager = ResultManager()
    private var loginProvider: LoginProvider? = null

    companion object {
        internal const val TAG = "CRouter"
        @SuppressLint("StaticFieldLeak")
        private var sInstance: CRouter = CRouter()

        /**
         * 添加拦截器
         */
        fun addInterceptor(interceptor: Interceptor) {
            sInstance.routerClient = routerClient().newBuilder().addInterceptor(interceptor).build()
        }

        /**
         * 设置登录提供者。设置后 [Router.needLogin] 才能生效
         */
        fun setLoginProvider(loginProvider: LoginProvider) {
            sInstance.loginProvider = loginProvider
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
            return resultManager().onActivityResult(requestCode, resultCode, data)
        }

        internal fun routerClient(): RouterClient {
            return sInstance.routerClient
        }

        internal fun routerStarter(): RouterStarter {
            return sInstance.routerStarter
        }

        internal fun resultManager(): ResultManager {
            return sInstance.resultManager
        }

        internal fun routerSet(): Set<Route> {
            return RouterSet.get()
        }

        internal fun loginProvider(): LoginProvider? {
            return sInstance.loginProvider
        }
    }
}
