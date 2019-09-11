package me.wcy.router.annotation

/**
 * 真正的路由信息
 */
interface Route {
    fun url(): String

    fun target(): Class<*>

    fun needLogin(): Boolean
}