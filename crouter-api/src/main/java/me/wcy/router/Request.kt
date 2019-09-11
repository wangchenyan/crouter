package me.wcy.router

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.support.v4.app.Fragment
import java.io.Serializable
import java.lang.ref.WeakReference

/**
 * 路由请求，用于设置请求信息
 */
class Request internal constructor(builder: Builder) {
    private val contextRef: WeakReference<Context>
    private val uri: Uri?
    private val target: Class<*>?
    private val intent: Intent?
    private val extras: Intent
    private val flags: Int
    private val needLogin: Boolean

    init {
        this.contextRef = builder.contextRef!!
        this.uri = builder.uri
        this.target = builder.target
        this.intent = builder.intent
        this.extras = builder.extras
        this.flags = builder.flags
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
     * 获取 URI
     */
    fun uri(): Uri? {
        return uri
    }

    /**
     * 获取目标类型
     */
    fun target(): Class<*>? {
        return target
    }

    /**
     * 获取意图
     */
    fun intent(): Intent? {
        return intent
    }

    /**
     * 获取额外参数
     */
    fun extras(): Intent {
        return extras
    }

    /**
     * 获取旗帜
     */
    fun flags(): Int {
        return flags
    }

    /**
     * 是否需要登录
     */
    fun needLogin(): Boolean {
        return needLogin
    }

    override fun toString(): String {
        return "Request(contextRef=$contextRef, uri=$uri, target=$target, intent=$intent, extras=$extras, flags=$flags, needLogin=$needLogin)"
    }

    /**
     * 路由请求构建器
     */
    class Builder {
        internal var contextRef: WeakReference<Context>? = null
        internal var uri: Uri? = null
        internal var target: Class<*>? = null
        internal var intent: Intent? = null
        internal var extras: Intent = Intent()
        internal var flags: Int = 0
        internal var needLogin = false

        constructor()

        internal constructor(request: Request) {
            this.contextRef = request.contextRef
            this.uri = request.uri
            this.target = request.target
            this.intent = request.intent
            this.extras = request.extras
            this.flags = request.flags
            this.needLogin = request.needLogin
        }

        /**
         * 设置上下文，必须
         */
        fun context(context: Context): Builder {
            contextRef = WeakReference(context)
            return this
        }

        /**
         * 设置 URI
         */
        fun uri(uri: Uri): Builder {
            this.uri = uri
            return this
        }

        /**
         * 设置 URL
         */
        fun url(url: String): Builder {
            return uri(Uri.parse(url))
        }

        /**
         * 设置目标类型
         */
        fun target(target: Class<*>): Builder {
            this.target = target
            return this
        }

        /**
         * 设置意图
         */
        fun intent(intent: Intent): Builder {
            this.intent = intent
            return this
        }

        /**
         * 设置旗帜
         */
        fun flags(flags: Int): Builder {
            this.flags = this.flags or flags
            return this
        }

        /**
         * 设置额外参数
         */
        fun extras(src: Intent): Builder {
            extras.putExtras(src)
            return this
        }

        /**
         * 设置额外参数
         */
        fun extra(name: String, value: Boolean): Builder {
            extras.putExtra(name, value)
            return this
        }

        /**
         * 设置额外参数
         */
        fun extra(name: String, value: Int): Builder {
            extras.putExtra(name, value)
            return this
        }

        /**
         * 设置额外参数
         */
        fun extra(name: String, value: Long): Builder {
            extras.putExtra(name, value)
            return this
        }

        /**
         * 设置额外参数
         */
        fun extra(name: String, value: Float): Builder {
            extras.putExtra(name, value)
            return this
        }

        /**
         * 设置额外参数
         */
        fun extra(name: String, value: Double): Builder {
            extras.putExtra(name, value)
            return this
        }

        /**
         * 设置额外参数
         */
        fun extra(name: String, value: String): Builder {
            extras.putExtra(name, value)
            return this
        }

        /**
         * 设置额外参数
         */
        fun extra(name: String, value: Serializable): Builder {
            extras.putExtra(name, value)
            return this
        }

        /**
         * 设置额外参数
         */
        fun extra(name: String, value: Parcelable): Builder {
            extras.putExtra(name, value)
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
         * 获取 Fragment
         */
        fun newFragment(): Fragment? {
            if (target != null && Fragment::class.java.isAssignableFrom(target)) {
                val instance = target!!.newInstance() as Fragment
                instance.arguments = extras.extras
                return instance
            }
            return null
        }

        /**
         * 构建请求
         */
        fun build(): Request {
            if (contextRef == null) {
                throw IllegalStateException("context == null")
            }
            return Request(this)
        }

        /**
         * 启动请求
         */
        fun start() {
            val request = build()
            CRouter.routerStarter().start(request)
        }

        /**
         * 启动请求，关注请求结果
         */
        fun startForResult(listener: ResultListener) {
            val request = build()
            CRouter.routerStarter().startForResult(request, listener)
        }
    }
}
