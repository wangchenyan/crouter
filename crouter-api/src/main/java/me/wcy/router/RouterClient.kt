package me.wcy.router

import android.content.Context
import me.wcy.router.annotation.Router
import java.util.Collections

/**
 * 路由客户端，在 CRouter 中只有一个
 */
class RouterClient : Call.Factory {
    private val interceptors: MutableList<Interceptor>
    private val loginProvider: ((context: Context, callback: () -> Unit) -> Unit)?

    constructor() : this(Builder())

    internal constructor(builder: Builder) {
        this.interceptors = Collections.unmodifiableList(ArrayList(builder.interceptors))
        this.loginProvider = builder.loginProvider

        if (interceptors.contains(null as Interceptor?)) {
            throw IllegalStateException("Null interceptor: $interceptors")
        }
    }

    fun interceptors(): List<Interceptor> {
        return interceptors
    }

    fun loginProvider(): ((context: Context, callback: () -> Unit) -> Unit)? {
        return loginProvider
    }

    override fun newCall(request: Request): Call {
        return RealCall.newRealCall(this, request)
    }

    fun newBuilder(): Builder {
        return Builder(this)
    }

    class Builder {
        internal val interceptors: MutableList<Interceptor> = ArrayList()
        internal var loginProvider: ((context: Context, callback: () -> Unit) -> Unit)? = null

        constructor()

        internal constructor(routerClient: RouterClient) {
            this.interceptors.addAll(routerClient.interceptors)
            this.loginProvider = routerClient.loginProvider
        }

        fun interceptors(): List<Interceptor> {
            return interceptors
        }

        /**
         * 添加拦截器
         */
        fun addInterceptor(interceptor: Interceptor): Builder {
            interceptors.add(interceptor)
            return this
        }

        /**
         * 设置登录提供者。设置后 [Router.needLogin] 才能生效
         */
        fun loginProvider(loginProvider: (context: Context, callback: () -> Unit) -> Unit): Builder {
            this.loginProvider = loginProvider
            return this
        }

        fun build(): RouterClient {
            return RouterClient(this)
        }
    }
}
