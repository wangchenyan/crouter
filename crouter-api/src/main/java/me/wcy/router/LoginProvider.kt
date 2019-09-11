package me.wcy.router

import android.content.Context

/**
 * 登录提供者
 */
interface LoginProvider {

    /**
     * 登录回调
     */
    interface Callback {
        /**
         * 登录成功
         */
        fun onLogin()
    }

    /**
     * 执行登录
     */
    fun login(context: Context, callback: Callback)
}