package com.padippob.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class UserSession @SuppressLint("CommitPrefEdits")
constructor(context: Context?) {
    private val sharedPreferences: SharedPreferences
    private val sharedPreferencesEditor: SharedPreferences.Editor

    init {
        sharedPreferences = context!!.getSharedPreferences(userData, Context.MODE_PRIVATE)
        sharedPreferencesEditor = sharedPreferences.edit()
    }

    fun saveString(key: String, value: String) {
        sharedPreferencesEditor.putString(key, value)
        sharedPreferencesEditor.commit()
    }

    fun saveInteger(key: String, value: Int?) {
        sharedPreferencesEditor.putInt(key, value!!)
        sharedPreferencesEditor.commit()
    }

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferencesEditor.putBoolean(key, value)
        sharedPreferencesEditor.commit()
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun getInteger(key: String): Int? {
        return sharedPreferences.getInt(key, 0)
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    companion object {
        private const val userData = "userData"
    }
}