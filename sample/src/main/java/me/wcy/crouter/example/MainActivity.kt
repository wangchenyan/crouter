package me.wcy.crouter.example

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import me.wcy.router.CRouter
import me.wcy.router.RouterClient

class MainActivity : BaseActivity() {
    private val button1: Button by lazy { findViewById(R.id.button1) }
    private val button2: Button by lazy { findViewById(R.id.button2) }
    private val button3: Button by lazy { findViewById(R.id.button3) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CRouter.setRouterClient(
            RouterClient.Builder()
                .loginProvider { context, callback ->
                    Toast.makeText(this@MainActivity, "拦截登录", Toast.LENGTH_SHORT).show()
                    CRouter.with(context)
                        .url("https://host.com/login.html")
                        .startForResult { resultCode, data ->
                            if (resultCode == Activity.RESULT_OK) {
                                callback.invoke()
                            }
                        }
                }
                .fragmentContainerIntent(Intent(this, FragmentContainerActivity::class.java))
                .build()
        )

        button1.setOnClickListener {
            CRouter.with(this)
                .url("https://host.com/target.html")
                .startForResult { resultCode, data ->
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        val value = data.extras?.getString("key")
                        alert("跳转取值", value)
                    }
                }
        }
        button2.setOnClickListener {
            CRouter.with(this)
                .url("https://host.com/target.html")
                .needLogin(true)
                .startForResult { resultCode, data ->
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        val value = data.extras?.getString("key")
                        alert("跳转取值", value)
                    }
                }
        }
        button3.setOnClickListener {
            CRouter.with(this)
                .url("https://host.com/fragment/my?key1=value1&key2=value2")
                .startForResult { resultCode, data ->
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        val value = data.extras?.getString("key")
                        alert("跳转取值", value)
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
