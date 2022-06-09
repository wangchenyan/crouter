package me.wcy.router.starter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import me.wcy.router.CRouter
import me.wcy.router.OnRouteResult
import me.wcy.router.Request
import me.wcy.router.Response
import me.wcy.router.ResultManager

/**
 * Created by wcy on 2020/12/30.
 */
class FragmentStarter : Fragment(), RouterStarter {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ResultManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun start(request: Request) {
        startForResult(request, null)
    }

    override fun startForResult(
        request: Request,
        listener: OnRouteResult?
    ) {
        val context = request.context()
        val client = CRouter.getRouterClient()
        val call = client.newCall(request)
        val response = call.execute()

        val loginProvider = client.loginProvider()
        if (response.needLogin() && loginProvider != null) {
            loginProvider.invoke(context) { realStartActivityForResult(response, listener) }
        } else {
            realStartActivityForResult(response, listener)
        }
    }


    private fun realStartActivityForResult(
        response: Response,
        listener: OnRouteResult?
    ) {
        val request = response.request()
        val context = request.context()
        if (response.intent() == null) {
            Log.e(CRouter.TAG, "no target found for request: $request")
            return
        }

        val intent = buildIntent(response)
        if (context is Activity && listener != null) {
            try {
                val requestCode = ResultManager.genRequestCode()
                startActivityForResult(intent, requestCode)
                ResultManager.add(requestCode, listener)
            } catch (e: Exception) {
                Log.e(CRouter.TAG, "start activity for result error, response=$response", e)
            }
        } else {
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Log.e(CRouter.TAG, "start activity error, response=$response", e)
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun buildIntent(response: Response): Intent {
        val request = response.request()
        val intent = response.intent()!!
        intent.putExtras(request.extras())
        if (request.uri() != null) {
            intent.data = request.uri()
        }
        if (request.flags() > 0) {
            intent.addFlags(request.flags())
        }
        return intent
    }
}