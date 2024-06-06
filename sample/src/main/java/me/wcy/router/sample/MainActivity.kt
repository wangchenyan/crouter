package me.wcy.router.sample

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
    private val buttonLib: Button by lazy { findViewById(R.id.buttonLib) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CRouter.setRouterClient(
            RouterClient.Builder()
                .baseUrl("https://host.com")
                .loginProvider { context, callback ->
                    Toast.makeText(this@MainActivity, "拦截登录", Toast.LENGTH_SHORT).show()
                    CRouter.with(context)
                        .url("https://host.com/login.html")
                        .startForResult {
                            if (it.isSuccess()) {
                                callback.invoke()
                            }
                        }
                }
                .fragmentContainerIntentProvider {
                    Intent(it, FragmentContainerActivity::class.java)
                }
                .build()
        )

        button1.setOnClickListener {
            CRouter.with(this)
                .url("/target.html")
                .startForResult {
                    if (it.isSuccess("key")) {
                        val value = it.data?.getStringExtra("key")
                        alert("跳转取值", value)
                    }
                }
        }
        button2.setOnClickListener {
            CRouter.with(this)
                .url("https://host.com/target.html")
                .needLogin(true)
                .startForResult {
                    if (it.isSuccess("key")) {
                        val value = it.data?.getStringExtra("key")
                        alert("跳转取值", value)
                    }
                }
        }
        button3.setOnClickListener {
            CRouter.with(this)
                .url("https://host.com/fragment/my?key1=value1&key2=value2")
                .startForResult {
                    if (it.isSuccess("key")) {
                        val value = it.data?.getStringExtra("key")
                        alert("跳转取值", value)
                    }
                }
        }
        buttonLib.setOnClickListener {
            CRouter.with(this)
                .url("/lib")
                .startForResult {
                    if (it.isSuccess("key")) {
                        val value = it.data?.getStringExtra("key")
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
