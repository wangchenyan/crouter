package me.wcy.crouter.example

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import me.wcy.router.CRouter
import me.wcy.router.RouterClient

class MainActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CRouter.setRouterClient(
            RouterClient.Builder()
                .loginProvider { context, callback ->
                    Toast.makeText(this@MainActivity, "拦截登录", Toast.LENGTH_SHORT).show()
                    CRouter.with(context)
                        .url("/login.html")
                        .startForResult { requestCode, resultCode, data ->
                            if (resultCode == Activity.RESULT_OK) {
                                callback.invoke()
                            }
                        }
                }
                .build()
        )
    }

    override fun onClick(v: View?) {
        when (v) {
            button1 -> {
                CRouter.with(this)
                    .url("/target.html")
                    .startForResult { requestCode, resultCode, data ->
                        if (resultCode == Activity.RESULT_OK && data != null) {
                            val value = data.extras?.getString("key")
                            alert("跳转取值", value)
                        }
                    }
            }
            button2 -> {
                CRouter.with(this)
                    .url("/target.html")
                    .needLogin(true)
                    .startForResult { requestCode, resultCode, data ->
                        if (resultCode == Activity.RESULT_OK && data != null) {
                            val value = data.extras?.getString("key")
                            alert("跳转取值", value)
                        }
                    }
            }
        }
    }

    private fun alert(title: String?, message: String?) {
        AlertDialog.Builder(this@MainActivity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("确定") { dialog, which -> }
            .show()
    }
}
