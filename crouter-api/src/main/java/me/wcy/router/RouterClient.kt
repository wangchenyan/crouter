package me.wcy.router

import android.content.Context
import android.content.Intent
import me.wcy.router.annotation.Router
import java.util.Collections

/**
 * 路由客户端，在 CRouter 中只有一个
 */
class RouterClient : Call.Factory {
    private val interceptors: MutableList<Interceptor>
    private val baseUrl: String?
    private val loginProvider: ((context: Context, callback: () -> Unit) -> Unit)?
    private val fragmentContainerIntentProvider: ((context: Context) -> Intent)?

    constructor() : this(Builder())

    internal constructor(builder: Builder) {
        this.interceptors = Collections.unmodifiableList(ArrayList(builder.interceptors))
        this.baseUrl = builder.baseUrl
        this.loginProvider = builder.loginProvider
        this.fragmentContainerIntentProvider = builder.fragmentContainerIntentProvider

        if (interceptors.contains(null as Interceptor?)) {
            throw IllegalStateException("Null interceptor: $interceptors")
        }
    }

    fun interceptors(): List<Interceptor> {
        return interceptors
    }

    fun baseUrl() = baseUrl

    fun loginProvider(): ((context: Context, callback: () -> Unit) -> Unit)? {
        return loginProvider
    }

    fun fragmentContainerIntentProvider(): ((context: Context) -> Intent)? {
        return fragmentContainerIntentProvider
    }

    override fun newCall(request: Request): Call {
        return RealCall.newRealCall(this, request)
    }

    fun newBuilder(): Builder {
        return Builder(this)
    }

    class Builder {
        internal val interceptors: MutableList<Interceptor> = ArrayList()
        internal var baseUrl: String? = null
        internal var loginProvider: ((context: Context, callback: () -> Unit) -> Unit)? = null
        internal var fragmentContainerIntentProvider: ((context: Context) -> Intent)? = null

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

        fun baseUrl(baseUrl: String): Builder {
            this.baseUrl = baseUrl
            return this
        }

        /**
         * 设置登录提供者。设置后 [Router.needLogin] 才能生效
         */
        fun loginProvider(loginProvider: (context: Context, callback: () -> Unit) -> Unit): Builder {
            this.loginProvider = loginProvider
            return this
        }

        /**
         * 启动 Fragment 时，需要设置 Fragment 容器 Activity 的 Intent
         */
        fun fragmentContainerIntentProvider(provider: (context: Context) -> Intent): Builder {
            this.fragmentContainerIntentProvider = provider
            return this
        }

        fun build(): RouterClient {
            return RouterClient(this)
        }
    }
}
