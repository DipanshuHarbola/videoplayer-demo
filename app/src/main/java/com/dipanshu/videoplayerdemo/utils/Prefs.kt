package com.dipanshu.videoplayerdemo.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.dipanshu.videoplayerdemo.utils.Constants.Companion.PREF_NAME

object Prefs {

    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE)
    }

    fun clear() {
        prefs?.edit()?.clear()?.apply()
    }

    fun getPrefsString(key: String): String? {
        return prefs?.getString(key, null)
    }

    fun setPrefs(key: String, value: String) {
        prefs?.edit()?.putString(key, value)?.apply()
    }

    fun removePref(key: String) {
        prefs?.edit()?.remove(key)?.apply()
    }
}