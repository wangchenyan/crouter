package me.wcy.crouter.example

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import me.wcy.router.annotation.Router

/**
 * Created by wcy on 2019-09-10.
 */
@Router("/login\\.html")
class LoginActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onClick(v: View?) {
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
        setResult(Activity.RESULT_OK)
        finish()
    }
}