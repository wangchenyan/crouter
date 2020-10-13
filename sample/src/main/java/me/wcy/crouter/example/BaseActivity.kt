package me.wcy.crouter.example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import me.wcy.router.CRouter
import me.wcy.router.RouterIntent

/**
 * Created by wcy on 2019-09-10.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        CRouter.onActivityResult(requestCode, resultCode, data)
    }

    override fun getIntent(): Intent {
        return RouterIntent(super.getIntent())
    }
}