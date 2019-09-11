package me.wcy.crouter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import me.wcy.router.annotation.Router

/**
 * Created by wcy on 2019-09-10.
 */
@Router("/target\\.html", needLogin = true)
class TargetActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target)
    }

    override fun onClick(v: View?) {
        val data = Intent()
        data.putExtra("key", "value from TargetActivity")
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}