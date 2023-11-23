package com.app.desafiodourado.core.sharedPreferences

interface SharedPreferencesBuilder {
    fun putString(key: String, data: String)
    fun putBoolean(key: String, data: Boolean)
    fun getString(key: String, defaultValue: String?): String?
    fun getBoolean(key: String, defaultValue: Boolean?): Boolean
}