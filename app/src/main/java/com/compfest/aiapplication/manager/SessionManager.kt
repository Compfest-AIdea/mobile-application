package com.compfest.aiapplication.manager

import android.content.Context
import com.compfest.aiapplication.EXTRA_NAME
import com.compfest.aiapplication.EXTRA_NICKNAME
import com.compfest.aiapplication.LOGIN_PREF

class SessionManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(LOGIN_PREF, Context.MODE_PRIVATE)

    fun saveSession(name: String, nickname: String) {
        val editor = sharedPreferences.edit()
        editor.putString(EXTRA_NAME, name)
        editor.putString(EXTRA_NICKNAME, nickname)
        editor.apply()
    }

    fun getName(): String? {
        return sharedPreferences.getString(EXTRA_NAME, "")
    }

    fun getNickname(): String? {
        return sharedPreferences.getString(EXTRA_NICKNAME, "")
    }
}