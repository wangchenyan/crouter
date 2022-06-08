package me.wcy.crouter.example

import android.net.Uri
import android.os.Bundle
import android.util.Log
import me.wcy.router.CRouter

/**
 * Created by wangchenyan.top on 2022/6/8.
 */
class FragmentContainerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri: Uri = intent.getParcelableExtra(CRouter.CROUTER_KEY_FRAGMENT_URI) ?: kotlin.run {
            if (BuildConfig.DEBUG) {
                throw IllegalStateException("FragmentContainerActivity only can be started by CRouter!")
            } else {
                Log.e(TAG, "FragmentContainerActivity only can be started by CRouter!")
                finish()
                return
            }
        }
        val clazz = CRouter.with(this).uri(uri).getFragmentX() ?: kotlin.run {
            if (BuildConfig.DEBUG) {
                throw IllegalStateException("Can not find fragment by uri: $uri")
            } else {
                Log.e(TAG, "Can not find fragment by uri: $uri")
                finish()
                return
            }
        }
        val fragment = clazz.newInstance()
        fragment.arguments = intent.extras
        fragment.arguments?.remove(CRouter.CROUTER_KEY_FRAGMENT_URI)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(android.R.id.content, fragment)
        transaction.commitNowAllowingStateLoss()
    }

    companion object {
        private const val TAG = "FragmentContainerAct"
    }
}