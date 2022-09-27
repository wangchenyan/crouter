package me.wcy.router

import android.content.Intent

/**
 * Activity 结果监听器
 */
@Deprecated("已过时", replaceWith = ReplaceWith("Kotlin 中使用更方便的高阶函数"))
interface ResultListener {
    fun onActivityResult(resultCode: Int, data: Intent?)
}

@Deprecated("已过时", replaceWith = ReplaceWith("OnRouteResultListener"))
typealias OnRouteResult = (resultCode: Int, data: Intent?) -> Unit

typealias OnRouteResultListener = (routeResult: RouteResult) -> Unit
