package com.gadgetsfury.electionindia.util

import android.content.Context

class SharedPref(val context: Context) {

    private val FIRST_TIME_USER = "first_time_user"
    private val LANGUAGE = "language"

    private val sharedPreference = context.getSharedPreferences("ElectionIndia", Context.MODE_PRIVATE)
    private val editor = sharedPreference.edit()

    fun dismissFirstTime() {
        editor.putBoolean(FIRST_TIME_USER, false)
        editor.commit()
    }

    fun isFirstTime(): Boolean {
        return sharedPreference.getBoolean(FIRST_TIME_USER, true)
    }

    fun setLanguage(lang: String) {
        editor.putString(LANGUAGE, lang)
        editor.commit()
    }

    fun getLanguage(): String {
        return sharedPreference.getString(LANGUAGE, "hi")!!
    }

}