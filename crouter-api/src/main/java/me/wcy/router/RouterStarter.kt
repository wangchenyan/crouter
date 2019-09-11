package me.wcy.router

/**
 * 路由响应启动器
 */
interface RouterStarter {

    fun start(request: Request)

    fun startForResult(request: Request, listener: ResultListener?)
}