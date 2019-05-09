package com.ziggy.kdo.network.configuration

import android.content.Context
import android.R.id.edit
import android.annotation.SuppressLint
import android.content.SharedPreferences


object UserSession {

    private const val KEY_PREFERENCE = "key_preferences"

    private const val KEY_TOKEN = "key_token"

    private const val KEY_TOKEN_REFRESH = "key_token_refresh"

    private const val KEY_UID = "key_uid"

    private fun getSharedPreference(context: Context):SharedPreferences{
        return context.getSharedPreferences(KEY_PREFERENCE, Context.MODE_PRIVATE)
    }

    fun getUserToken(context: Context):String? {
        val editor = getSharedPreference(context)
        return editor.getString(KEY_TOKEN, "")!!
    }

    fun getUserRefreshToken(context: Context):String? {
        val editor = getSharedPreference(context)
        return editor.getString(KEY_TOKEN_REFRESH, "")!!
    }

    fun getUid(context: Context):String? {
        val editor = getSharedPreference(context)
        return editor.getString(KEY_TOKEN_REFRESH, "")!!
    }

    @SuppressLint("ApplySharedPref")
    fun createUserToken(context: Context, token: String?, tokenRefresh: String?, uid: String?) {
        val editor = getSharedPreference(context).edit()
        editor.putString(KEY_TOKEN, "bearer $token")
        editor.putString(KEY_TOKEN_REFRESH, tokenRefresh)
        editor.putString(KEY_UID, uid)
        editor.commit()
    }

    fun deleteUserSession(context: Context) {
        context.getSharedPreferences(KEY_PREFERENCE, Context.MODE_PRIVATE).edit().clear().apply()
    }
}