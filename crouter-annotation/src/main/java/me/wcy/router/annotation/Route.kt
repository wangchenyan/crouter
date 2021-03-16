package me.wcy.router.annotation

/**
 * 真正的路由信息
 */
abstract class Route {
    abstract fun path(): String

    abstract fun target(): Class<*>

    abstract fun needLogin(): Boolean

    override fun hashCode(): Int {
        return path().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Route) {
            return path() == other.path()
        }
        return false
    }
}