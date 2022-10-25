package me.wcy.router.starter

import me.wcy.router.RouteResultListener
import me.wcy.router.Request

/**
 * 路由响应启动器
 */
interface RouterStarter {

    fun start(request: Request)

    fun startForResult(
        request: Request,
        listener: RouteResultListener?
    )
}