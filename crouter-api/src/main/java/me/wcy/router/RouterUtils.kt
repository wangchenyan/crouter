package me.wcy.router

import android.net.Uri
import me.wcy.router.annotation.Route
import java.lang.IllegalArgumentException

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
        val list = url.split(Regex("://"))
        if (list.size != 2) {
            throw IllegalArgumentException("url '$url' is illegal")
        }
        val scheme = list[0]
        var path = list[1].replace(Regex("/+"), "/")
        if (path.endsWith("/")) {
            path = path.dropLast(1)
        }
        return "$scheme://$path"
    }
}