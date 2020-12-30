package me.wcy.crouter.example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import me.wcy.router.RouterIntent

/**
 * Created by wcy on 2019-09-10.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun getIntent(): Intent {
        return RouterIntent(super.getIntent())
    }
}