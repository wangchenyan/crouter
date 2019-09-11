package me.wcy.router

import java.util.*

/**
 * 路由客户端，在 CRouter 中只有一个
 */
class RouterClient : Call.Factory {
    internal val interceptors: MutableList<Interceptor>

    constructor() : this(Builder())

    internal constructor(builder: Builder) {
        this.interceptors = Collections.unmodifiableList(ArrayList(builder.interceptors))

        if (interceptors.contains(null)) {
            throw IllegalStateException("Null interceptor: $interceptors")
        }
    }

    fun interceptors(): List<Interceptor> {
        return interceptors
    }

    override fun newCall(request: Request): Call {
        return RealCall.newRealCall(this, request)
    }

    fun newBuilder(): Builder {
        return Builder(this)
    }

    class Builder {
        internal val interceptors: MutableList<Interceptor> = ArrayList()

        constructor()

        internal constructor(routerClient: RouterClient) {
            this.interceptors.addAll(routerClient.interceptors)
        }

        fun interceptors(): List<Interceptor> {
            return interceptors
        }

        fun addInterceptor(interceptor: Interceptor): Builder {
            interceptors.add(interceptor)
            return this
        }

        fun build(): RouterClient {
            return RouterClient(this)
        }
    }
}
