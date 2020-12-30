package me.wcy.router.starter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import me.wcy.router.CRouter

/**
 * Created by wcy on 2020/12/30.
 */
object RouterStarterFactory {
    private const val FRAGMENT_TAG = "crouter_fragment_tag"

    fun create(context: Context): RouterStarter {
        if (context is FragmentActivity) {
            val supportFragmentManager = getSupportFragmentManager(context)
            var permissionSupportFragment =
                supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as SupportFragmentStarter?
            if (permissionSupportFragment == null) {
                permissionSupportFragment = SupportFragmentStarter()
                supportFragmentManager.beginTransaction()
                    .add(permissionSupportFragment, FRAGMENT_TAG)
                    .commitNowAllowingStateLoss()
            }
            return permissionSupportFragment
        } else if (context is Activity) {
            val fragmentManager = getFragmentManager(context)
            var permissionFragment =
                fragmentManager.findFragmentByTag(FRAGMENT_TAG) as FragmentStarter?
            if (permissionFragment == null) {
                permissionFragment = FragmentStarter()
                fragmentManager.beginTransaction()
                    .add(permissionFragment, FRAGMENT_TAG)
                    .commitAllowingStateLoss()
                // make it commit like commitNow
                fragmentManager.executePendingTransactions()
            }
            return permissionFragment
        } else {
            return ContextStarter(context)
        }
    }

    private fun getSupportFragmentManager(activity: FragmentActivity): FragmentManager {
        val fragmentManager = activity.supportFragmentManager
        // some specific rom will provide a null List
        val childAvailable = fragmentManager.fragments != null
        if (childAvailable
            && fragmentManager.fragments.isNotEmpty()
            && fragmentManager.fragments[0] != null
        ) {
            return fragmentManager.fragments[0].childFragmentManager
        } else {
            return fragmentManager
        }
    }

    @SuppressLint("PrivateApi")
    private fun getFragmentManager(activity: Activity): android.app.FragmentManager {
        val fragmentManager = activity.fragmentManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (fragmentManager.fragments != null
                && fragmentManager.fragments.isNotEmpty()
                && fragmentManager.fragments[0] != null
            ) {
                return fragmentManager.fragments[0].childFragmentManager
            }
        } else {
            try {
                val fragmentsField = Class.forName("android.app.FragmentManagerImpl")
                    .getDeclaredField("mAdded")
                fragmentsField.isAccessible = true
                val fragmentList = fragmentsField[fragmentManager] as List<Fragment?>
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                    && fragmentList != null
                    && fragmentList.isNotEmpty()
                    && fragmentList[0] != null
                ) {
                    Log.d(CRouter.TAG, "reflect get child fragmentManager success")
                    return fragmentList[0]!!.childFragmentManager
                }
            } catch (e: Exception) {
                Log.w(CRouter.TAG, "try to get childFragmentManager failed $e")
                e.printStackTrace()
            }
        }
        return fragmentManager
    }
}