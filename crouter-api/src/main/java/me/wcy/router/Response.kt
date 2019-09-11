package me.wcy.router

import android.content.Context
import android.content.Intent

import java.lang.ref.WeakReference

/**
 * 路由响应，包含路由请求，真实意图等信息
 */
class Response internal constructor(builder: Builder) {
    private val contextRef: WeakReference<Context>
    private val request: Request
    private val intent: Intent?
    private val needLogin: Boolean

    init {
        this.contextRef = builder.contextRef!!
        this.request = builder.request!!
        this.intent = builder.intent
        this.needLogin = builder.needLogin
    }

    /**
     * 生成新的构建器
     */
    fun newBuilder(): Builder {
        return Builder(this)
    }

    /**
     * 获取上下文
     */
    fun context(): Context {
        return contextRef.get() ?: throw IllegalStateException("context is recycled")
    }

    /**
     * 获取路由请求
     */
    fun request(): Request {
        return request
    }

    /**
     * 获取意图
     */
    fun intent(): Intent? {
        return intent
    }

    /**
     * 是否需要登录
     */
    fun needLogin(): Boolean {
        return needLogin
    }

    override fun toString(): String {
        return "Response(contextRef=$contextRef, request=$request, intent=$intent, needLogin=$needLogin)"
    }

    /**
     * 路由响应构建器
     */
    class Builder {
        internal var contextRef: WeakReference<Context>? = null
        internal var request: Request? = null
        internal var intent: Intent? = null
        internal var needLogin = false

        constructor()

        internal constructor(response: Response) {
            this.contextRef = response.contextRef
            this.request = response.request
            this.intent = response.intent
            this.needLogin = response.needLogin
        }

        /**
         * 设置上下文，必须
         */
        fun context(context: Context): Builder {
            contextRef = WeakReference(context)
            return this
        }

        /**
         * 设置路由请求，必须
         */
        fun request(request: Request): Builder {
            this.request = request
            return this
        }

        /**
         * 设置真实意图
         */
        fun intent(intent: Intent): Builder {
            this.intent = intent
            return this
        }

        /**
         * 设置是否需要登录
         */
        fun needLogin(needLogin: Boolean): Builder {
            this.needLogin = needLogin
            return this
        }

        /**
         * 构建路由响应
         */
        fun build(): Response {
            if (contextRef == null) {
                throw IllegalStateException("context == null")
            }
            if (request == null) {
                throw IllegalStateException("request == null")
            }
            return Response(this)
        }
    }
}
