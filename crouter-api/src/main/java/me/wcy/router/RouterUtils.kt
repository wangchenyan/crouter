package me.wcy.router

import android.app.Activity
import android.net.Uri
import android.os.Build
import me.wcy.router.annotation.Route

/**
 * Created by wcy on 2019/7/13.
 */
object RouterUtils {

    /**
     * 判断路由是否匹配 URL
     */
    fun match(route: Route, uri: Uri): Boolean {
        val routeUrl = route.url().formatUrl()
        var targetUrl = if (uri.scheme.isNullOrEmpty()
            && uri.host.isNullOrEmpty()
            && CRouter.getRouterClient().baseUrl()?.isNotEmpty() == true
        ) {
            CRouter.getRouterClient().baseUrl() + uri.path
        } else {
            uri.scheme + "://" + uri.host + uri.path
        }
        targetUrl = targetUrl.formatUrl()
        return Regex(routeUrl).matches(targetUrl)
    }

    /**
     * 格式化 URL
     * - 移除重复的 '/'
     * - 移除最后一个 '/'
     */
    private fun String.formatUrl(): String {
        val list = this.split(Regex("://"))
        if (list.size != 2) {
            throw IllegalArgumentException("url '$this' is illegal")
        }
        val scheme = list[0]
        var path = list[1].replace(Regex("/+"), "/")
        if (path.endsWith("/")) {
            path = path.dropLast(1)
        }
        return "$scheme://$path"
    }

    fun isActivityAlive(activity: Activity): Boolean {
        return activity.isFinishing.not()
                && (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || activity.isDestroyed.not())
    }
}