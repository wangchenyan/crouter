package me.wcy.router.sample.lib

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import me.wcy.router.annotation.Route

/**
 * Created by wangchenyan.top on 2024/6/5.
 */
@Route("/lib")
class LibActivity : AppCompatActivity(), OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lib)
    }

    override fun onClick(v: View?) {
        val data = Intent()
        data.putExtra("key", "value from LibActivity")
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}