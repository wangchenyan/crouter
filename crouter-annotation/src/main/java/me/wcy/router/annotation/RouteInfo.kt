package me.wcy.router.annotation

/**
 * 真正的路由信息
 */
abstract class RouteInfo {
    abstract fun url(): String

    abstract fun target(): Class<*>

    abstract fun needLogin(): Boolean

    override fun hashCode(): Int {
        return url().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is RouteInfo) {
            return url() == other.url()
        }
        return false
    }
}