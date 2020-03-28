package com.shakib.digitalcom.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesSingleton {

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE)
    }

    fun saveString(key: String?, value: String?) {
        val prefsEditor = sharedPreferences.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    fun saveBoolean(key: String?, bool: Boolean?) {
        val prefsEditor = sharedPreferences.edit()
        prefsEditor.putBoolean(key, bool!!)
        prefsEditor.apply()
    }

    fun saveInt(key: String?, int: Int?){
        val prefsEditor = sharedPreferences.edit()
        prefsEditor.putInt(key, int!!)
        prefsEditor.apply()
    }

    fun getString(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }

    fun getBoolean(key: String?): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getInt(key: String?, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun clearData() {
        val prefsEditor = sharedPreferences.edit()
        prefsEditor.clear()
        prefsEditor.apply()
    }
}