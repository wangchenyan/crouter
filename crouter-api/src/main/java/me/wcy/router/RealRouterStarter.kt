package me.wcy.router

import android.app.Activity
import android.content.Intent
import android.util.Log

/**
 * 路由响应启动器实现类
 */
internal class RealRouterStarter : RouterStarter {

    override fun start(request: Request) {
        startForResult(request, null)
    }

    override fun startForResult(request: Request, listener: ResultListener?) {
        val context = request.context()
        val client = CRouter.routerClient()
        val call = client.newCall(request)
        val response = call.execute()

        val loginProvider = CRouter.loginProvider()
        if (response.needLogin() && loginProvider != null) {
            loginProvider.login(context, object : LoginProvider.Callback {
                override fun onLogin() {
                    realStartActivityForResult(response, listener)
                }
            })
        } else {
            realStartActivityForResult(response, listener)
        }
    }

    private fun realStartActivityForResult(response: Response, listener: ResultListener?) {
        val request = response.request()
        val context = request.context()
        if (response.intent() == null) {
            Log.e(CRouter.TAG, "no target found for request: $request")
        } else {
            val intent = buildIntent(response)
            if (context is Activity && listener != null) {
                try {
                    val requestCode = CRouter.resultManager().genRequestCode()
                    context.startActivityForResult(intent, requestCode)
                    CRouter.resultManager().add(requestCode, listener)
                } catch (e: Exception) {
                    Log.e(CRouter.TAG, "start activity for result error, response=$response", e)
                }
            } else {
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Log.e(CRouter.TAG, "start activity error, response=$response", e)
                }
            }
        }
    }

    private fun buildIntent(response: Response): Intent {
        val context = response.context()
        val request = response.request()
        val intent = response.intent()!!
        intent.putExtras(request.extras())
        if (request.uri() != null) {
            intent.data = request.uri()
        }
        if (request.flags() > 0) {
            intent.addFlags(request.flags())
        }
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return intent
    }
}
