package me.wcy.router

import android.content.Intent

/**
 * 路由响应启动器
 */
interface RouterStarter {

    fun start(request: Request)

    fun startForResult(
        request: Request,
        listener: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)?
    )
}