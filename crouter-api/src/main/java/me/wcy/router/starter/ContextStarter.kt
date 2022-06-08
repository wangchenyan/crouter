package me.wcy.router.starter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import me.wcy.router.CRouter
import me.wcy.router.Request
import me.wcy.router.Response

/**
 * 默认的启动器
 */
class ContextStarter(private val context: Context) :
    RouterStarter {

    override fun start(request: Request) {
        startForResult(request, null)
    }

    override fun startForResult(
        request: Request,
        listener: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)?
    ) {
        if (listener != null) {
            throw IllegalStateException("start for result should use activity context!")
        }
        val client = CRouter.getRouterClient()
        val call = client.newCall(request)
        val response = call.execute()

        val loginProvider = client.loginProvider()
        if (response.needLogin() && loginProvider != null) {
            loginProvider.invoke(context) {
                realStartActivity(response)
            }
        } else {
            realStartActivity(response)
        }
    }

    private fun realStartActivity(
        response: Response
    ) {
        val request = response.request()
        if (response.intent() == null) {
            Log.e(CRouter.TAG, "no target found for request: $request")
            return
        }

        val intent = buildIntent(response)
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(CRouter.TAG, "start activity error, response=$response", e)
        }
    }

    @SuppressLint("WrongConstant")
    private fun buildIntent(response: Response): Intent {
        val request = response.request()
        val intent = response.intent()!!
        intent.putExtras(request.extras())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (request.uri() != null) {
            intent.data = request.uri()
        }
        if (request.flags() > 0) {
            intent.addFlags(request.flags())
        }
        return intent
    }
}