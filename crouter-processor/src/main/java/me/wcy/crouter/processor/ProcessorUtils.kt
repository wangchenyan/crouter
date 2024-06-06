package me.wcy.crouter.processor

import me.wcy.router.annotation.Route

/**
 * Created by wcy on 2019/7/19.
 */
object ProcessorUtils {

    fun formatModuleName(moduleName: String): String {
        return moduleName.replace('-', '_')
    }

    fun assembleRouteUrl(route: Route, defaultScheme: String, defaultHost: String): String {
        val scheme = route.scheme.ifEmpty { defaultScheme }
        val host = route.host.ifEmpty { defaultHost }
        val path = route.value
        if (scheme.contains(":") || scheme.contains("/")) {
            throw IllegalArgumentException("[CRouter] Scheme '$scheme' must not be null and must not contains ':' or '/'")
        }
        if (host.contains('/')) {
            throw IllegalArgumentException("[CRouter] Host '$host' must not be null and must not contains '/'")
        }
        if (path.isNotEmpty() && !path.startsWith('/')) {
            throw IllegalArgumentException("[CRouter] Path '$path' must be '' or start with '/'")
        }

        return "$scheme://$host$path"
    }

    fun escapeUrl(url: String): String {
        return "\"" + url.replace("\\", "\\\\") + "\""
    }
}