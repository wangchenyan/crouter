package me.wcy.router.annotation

import kotlin.reflect.KClass

/**
 * 真正的路由信息
 */
data class RouteMeta(
    val url: String,
    val target: KClass<*>,
    val needLogin: Boolean
) {
    override fun hashCode(): Int {
        return url.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is RouteMeta) {
            return url == other.url
        }
        return false
    }
}