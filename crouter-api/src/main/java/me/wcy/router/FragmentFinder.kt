package me.wcy.router

import androidx.fragment.app.Fragment
import me.wcy.router.annotation.Route

/**
 * Created by wangchenyan.top on 2022/6/8.
 */
internal object FragmentFinder {

    fun findFragmentX(request: Request): Class<out Fragment>? {
        val uri = request.uri() ?: return null
        CRouter.getRouteSet().find { route: Route ->
            RouterUtils.match(route, uri) && isFragmentX(route.target())
        }?.let {
            return it.target() as Class<out Fragment>
        }
        return null
    }

    fun findFragment(request: Request): Class<out android.app.Fragment>? {
        val uri = request.uri() ?: return null
        CRouter.getRouteSet().find { route: Route ->
            RouterUtils.match(route, uri) && isFragment(route.target())
        }?.let {
            return it.target() as Class<out android.app.Fragment>
        }
        return null
    }

    fun isAnyFragment(target: Class<*>): Boolean {
        return isFragment(target) || isFragmentX(target)
    }

    private fun isFragment(target: Class<*>): Boolean {
        return android.app.Fragment::class.java.isAssignableFrom(target)
    }

    private fun isFragmentX(target: Class<*>): Boolean {
        return Fragment::class.java.isAssignableFrom(target)
    }
}