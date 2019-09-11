package me.wcy.crouter.example

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import me.wcy.router.CRouter

/**
 * Created by wcy on 2019-09-10.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        CRouter.onActivityResult(requestCode, resultCode, data)
    }
}