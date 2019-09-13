package org.lindelin.lindale.supports

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

/**
 * [SharedPreferences] Support
 */
class Preferences(context: Context) {

    //private val data: SharedPreferences = context.getSharedPreferences("${context.packageName}_preferences", Context.MODE_PRIVATE)
    private val data: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun putString(key: String, value: String?) = transaction { putString(key, value) }

    fun getString(key: String, default: String? = null) = data.getString(key, default)

    fun putStringSet(key: String, values: Set<String>?) = transaction { putStringSet(key, values) }
    fun getStringSet(key: String, default: Set<String>? = null) = data.getStringSet(key, default)

    fun putInt(key: String, value: Int?) = transaction { putInt(key, value) }

    fun getInt(key: String, default: Int) = data.getInt(key, default)
    fun getInt(key: String) = when {
        contains(key) -> getInt(key, 0)
        else -> null
    }

    fun putLong(key: String, value: Long?) = transaction { putLong(key, value) }

    fun getLong(key: String, default: Long) = data.getLong(key, default)
    fun getLong(key: String) = when {
        contains(key) -> getLong(key, 0)
        else -> null
    }

    fun putFloat(key: String, value: Float?) = transaction { putFloat(key, value) }

    fun getFloat(key: String, default: Float) = data.getFloat(key, default)
    fun getFloat(key: String) = when {
        contains(key) -> getFloat(key, 0f)
        else -> null
    }

    fun putBoolean(key: String, value: Boolean?) = transaction { putBoolean(key, value) }

    fun getBoolean(key: String, default: Boolean = false) = data.getBoolean(key, default)
    fun getBoolean(key: String) = when {
        contains(key) -> getBoolean(key, false)
        else -> null
    }

    private fun contains(key: String) = data.contains(key)

    fun remove(key: String) = transaction { remove(key) }

    fun clear(vararg keys: String) = transaction { keys.filter { it.isNotEmpty() }.forEach { remove(it) } }

    @SuppressLint("ApplySharedPref")
    fun transactionImmediately(t: SharedPreferences.Editor.() -> Unit) = data.edit().also(t).commit()

    fun transaction(t: SharedPreferences.Editor.() -> Unit) = data.edit().also(t).apply()
}

// SharedPreferences.Editor Extension
fun SharedPreferences.Editor.putInt(key: String, value: Int?) = value?.let { putInt(key, it) } ?: remove(key)
fun SharedPreferences.Editor.putLong(key: String, value: Long?) = value?.let { putLong(key, it) } ?: remove(key)
fun SharedPreferences.Editor.putFloat(key: String, value: Float?) = value?.let { putFloat(key, it) } ?: remove(key)
fun SharedPreferences.Editor.putBoolean(key: String, value: Boolean?) = value?.let { putBoolean(key, it) } ?: remove(key)