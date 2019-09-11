package me.wcy.router

import android.app.Activity
import android.content.Intent

/**
 * 意图包装类，需要替换 Activity 基类中的 [Activity.getIntent]
 *
 * 示例代码
 *
 * ```
 * override fun getIntent(): Intent {
 *     return RouterIntent(super.getIntent())
 * }
 * ```
 */
class RouterIntent(intent: Intent) : Intent(intent) {

    private fun get(name: String?): Any? {
        return extras?.get(name)
    }

    override fun getBooleanExtra(name: String?, defaultValue: Boolean): Boolean {
        val value = get(name)
        if (value is Boolean) {
            return value
        } else if (value is String) {
            return java.lang.Boolean.parseBoolean(value)
        }
        return defaultValue
    }

    override fun getByteExtra(name: String?, defaultValue: Byte): Byte {
        val value = get(name)
        if (value is Byte) {
            return value
        } else if (value is String) {
            try {
                return java.lang.Byte.parseByte(value)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
        return defaultValue
    }

    override fun getIntExtra(name: String?, defaultValue: Int): Int {
        val value = get(name)
        if (value is Int) {
            return value
        } else if (value is String) {
            try {
                return Integer.parseInt(value)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
        return defaultValue
    }

    override fun getCharExtra(name: String?, defaultValue: Char): Char {
        val value = get(name)
        if (value is Char) {
            return value
        } else if (value is String && value.isNotEmpty()) {
            return value[0]
        }
        return defaultValue
    }

    override fun getLongExtra(name: String?, defaultValue: Long): Long {
        val value = get(name)
        if (value is Long) {
            return value
        } else if (value is String) {
            try {
                return java.lang.Long.parseLong(value)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
        return defaultValue
    }

    override fun getFloatExtra(name: String?, defaultValue: Float): Float {
        val value = get(name)
        if (value is Float) {
            return value
        } else if (value is String) {
            try {
                return java.lang.Float.parseFloat(value)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
        return defaultValue
    }

    override fun getDoubleExtra(name: String?, defaultValue: Double): Double {
        val value = get(name)
        if (value is Double) {
            return value
        } else if (value is String) {
            try {
                return java.lang.Double.parseDouble(value)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
        return defaultValue
    }
}
