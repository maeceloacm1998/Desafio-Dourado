package com.app.desafiodourado.core.sharedPreferences

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesBuilderImpl(context: Context): SharedPreferencesBuilder {
    private val sharedPref =
        context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    override fun putString(key: String, data: String) {
        editor.putString(key, data).commit()
    }

    override fun putBoolean(key: String, data: Boolean) {
        editor.putBoolean(key, data).commit()
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return sharedPref.getString(key, defaultValue ?: "")
    }

    override fun getBoolean(key: String, defaultValue: Boolean?): Boolean {
        return sharedPref.getBoolean(key, defaultValue ?: false)
    }

    companion object {
        const val SHARED_PREFERENCES_KEY = "@desafio_dourado"
    }
}
