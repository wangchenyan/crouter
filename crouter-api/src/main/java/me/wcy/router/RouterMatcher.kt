package me.wcy.router

import android.net.Uri
import me.wcy.router.annotation.Route

/**
 * Created by wcy on 2019/7/13.
 */
object RouterMatcher {

    /**
     * 判断路由是否匹配 URL
     */
    fun match(route: Route, uri: Uri): Boolean {
        val routePath = formatPath(route.path())
        val targetPath = formatPath(uri.path ?: "")
        return Regex(routePath).matches(targetPath)
    }

    /**
     * 格式化 URL，移除重复的 '/' 和最后一个 '/'
     */
    private fun formatPath(path: String): String {
        var mPath = path.replace(Regex("/+"), "/")
        if (mPath.endsWith("/")) {
            mPath = mPath.dropLast(1)
        }
        return mPath
    }
}