package me.wcy.router

import android.content.Intent

/**
 * Activity 结果监听器
 */
@Deprecated("已过时", replaceWith = ReplaceWith("Kotlin 中使用更方便的 Lambda 表达式"))
interface ResultListener {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}
