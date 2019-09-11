package me.wcy.crouter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import me.wcy.router.CRouter
import me.wcy.router.LoginProvider
import me.wcy.router.ResultListener

class MainActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CRouter.setLoginProvider(object : LoginProvider {
            override fun login(context: Context, callback: LoginProvider.Callback) {
                CRouter.with(context)
                    .url("https://host.com/login.html")
                    .startForResult(object : ResultListener {
                        override fun onActivityResult(
                            requestCode: Int,
                            resultCode: Int,
                            data: Intent?
                        ) {
                            if (resultCode == Activity.RESULT_OK) {
                                callback.onLogin()
                            }
                        }
                    })
            }
        })
    }

    override fun onClick(v: View?) {
        when (v) {
            button1 -> {
                CRouter.with(this)
                    .url("https://host.com/target.html").startForResult(object : ResultListener {
                        override fun onActivityResult(
                            requestCode: Int,
                            resultCode: Int,
                            data: Intent?
                        ) {
                            if (resultCode == Activity.RESULT_OK && data != null) {
                                val value = data.extras?.getString("key")
                                Toast.makeText(this@MainActivity, "跳转取值：$value", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    })
            }
        }
    }
}
