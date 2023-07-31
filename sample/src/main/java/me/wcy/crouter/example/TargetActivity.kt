package me.wcy.crouter.example

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import me.wcy.router.annotation.Route

/**
 * Created by wcy on 2019-09-10.
 */
@Route("/target\\.html")
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