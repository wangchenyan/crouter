package me.wcy.router

import android.content.Intent

/**
 * Activity 结果监听器
 */
interface ResultListener {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}