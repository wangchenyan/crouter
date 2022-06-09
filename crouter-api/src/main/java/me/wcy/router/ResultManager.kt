package me.wcy.router

import android.content.Intent
import android.util.SparseArray

import java.util.concurrent.atomic.AtomicInteger

/**
 * Activity 结果管理器
 */
internal object ResultManager {
    private val resultMap = SparseArray<OnRouteResult>()
    private val requestCode = AtomicInteger(0)

    fun genRequestCode(): Int {
        return requestCode.incrementAndGet()
    }

    fun add(
        requestCode: Int,
        listener: OnRouteResult
    ) {
        resultMap.put(requestCode, listener)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        val callback = resultMap.get(requestCode)
        if (callback != null) {
            callback.invoke(resultCode, data)
            resultMap.remove(requestCode)
            return true
        }
        return false
    }
}
