package me.wcy.router

import androidx.fragment.app.Fragment
import me.wcy.router.annotation.RouteInfo
import kotlin.reflect.KClass

/**
 * Created by wangchenyan.top on 2022/6/8.
 */
internal object FragmentFinder {

    fun findFragmentX(request: Request): KClass<out Fragment>? {
        val uri = request.uri() ?: return null
        CRouter.getRouteSet().find { route: RouteInfo ->
            RouterUtils.match(route, uri) && isFragmentX(route.target)
        }?.let {
            return it.target as KClass<out Fragment>
        }
        return null
    }

    fun findFragment(request: Request): KClass<out android.app.Fragment>? {
        val uri = request.uri() ?: return null
        CRouter.getRouteSet().find { route: RouteInfo ->
            RouterUtils.match(route, uri) && isFragment(route.target)
        }?.let {
            return it.target as KClass<out android.app.Fragment>
        }
        return null
    }

    fun isAnyFragment(target: KClass<*>): Boolean {
        return isFragment(target) || isFragmentX(target)
    }

    private fun isFragment(target: KClass<*>): Boolean {
        return android.app.Fragment::class.java.isAssignableFrom(target.java)
    }

    private fun isFragmentX(target: KClass<*>): Boolean {
        return Fragment::class.java.isAssignableFrom(target.java)
    }
}