package me.wcy.router

import android.net.Uri
import me.wcy.router.annotation.Route

/**
 * Created by wcy on 2019/7/13.
 */
object RouterUtils {

    /**
     * 判断路由是否匹配 URL
     */
    fun match(route: Route, uri: Uri): Boolean {
        val routeUrl = formatUrl(route.url())
        val targetUrl = formatUrl(uri.scheme + "://" + uri.host + uri.path)
        return Regex(routeUrl).matches(targetUrl)
    }

    /**
     * 格式化 URL，移除重复的 '/' 和最后一个 '/'
     */
    private fun formatUrl(url: String): String {
        var result = url.replace(Regex("/+"), "/")
                .replace(":/", "://")
        if (result.endsWith("/")) {
            result = result.dropLast(1)
        }
        return result
    }
}