package me.wcy.crouter.example

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import me.wcy.router.annotation.Route

/**
 * Created by wcy on 2019-09-10.
 */
@Route("/login\\.html")
class LoginActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onClick(v: View?) {
        AlertDialog.Builder(this)
            .setTitle("登录")
            .setMessage("登录成功")
            .setPositiveButton("确定") { dialog, which ->
                setResult(Activity.RESULT_OK)
                finish()
            }
            .show()
    }
}