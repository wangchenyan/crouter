package me.wcy.router.starter

import android.content.Intent
import me.wcy.router.Request

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