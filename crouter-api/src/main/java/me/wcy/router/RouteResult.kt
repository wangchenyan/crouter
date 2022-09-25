package me.wcy.router

import android.app.Activity
import android.content.Intent

/**
 * Created by wangchenyan.top on 2022/9/25.
 */
data class RouteResult(
    val resultCode: Int,
    val data: Intent?
) {

    fun isSuccess(vararg keys: String): Boolean {
        return resultCode == Activity.RESULT_OK && hasExtras(*keys)
    }

    private fun hasExtras(vararg keys: String): Boolean {
        keys.forEach {
            if (data?.hasExtra(it) != true) {
                return false
            }
        }
        return true
    }
}
