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

    private val KEY_MAIL = "key_mail"

    private val KEY_FIRSTNAME = "key_firstname"

    private val KEY_LASTNAME = "key_lastname"

    private val KEY_IMG_PROFIL = "key_img_profil"

    private fun getSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(KEY_PREFERENCE, Context.MODE_PRIVATE)
    }

    fun getUserToken(context: Context): String? {
        val editor = getSharedPreference(context)
        return editor.getString(KEY_TOKEN, "")!!
    }

    fun getUserRefreshToken(context: Context): String? {
        val editor = getSharedPreference(context)
        return editor.getString(KEY_TOKEN_REFRESH, "")!!
    }

    fun getUid(context: Context): String? {
        val editor = getSharedPreference(context)
        return editor.getString(KEY_UID, "")!!
    }

    fun getMail(context: Context): String? {
        val editor = getSharedPreference(context)
        return editor.getString(KEY_MAIL, "")!!
    }

    fun getFirstname(context: Context): String? {
        val editor = getSharedPreference(context)
        return editor.getString(KEY_FIRSTNAME, "")!!
    }

    fun getLastname(context: Context): String? {
        val editor = getSharedPreference(context)
        return editor.getString(KEY_LASTNAME, "")!!
    }


    fun getPhoto(context: Context): String? {
        val editor = getSharedPreference(context)
        return editor.getString(KEY_IMG_PROFIL, "")!!
    }

    @SuppressLint("ApplySharedPref")
    fun createUserToken(
        context: Context,
        token: String?,
        tokenRefresh: String?,
        uid: String?,
        mail: String?,
        firstname: String?,
        lastname: String?,
        imgProfil: String?
    ) {
        val editor = getSharedPreference(context).edit()
        editor.putString(KEY_TOKEN, "bearer $token")
        editor.putString(KEY_TOKEN_REFRESH, tokenRefresh)
        editor.putString(KEY_UID, uid)
        editor.putString(KEY_MAIL, mail)
        editor.putString(KEY_FIRSTNAME, firstname)
        editor.putString(KEY_LASTNAME, lastname)
        editor.putString(KEY_IMG_PROFIL, imgProfil)
        editor.commit()
    }

    fun setToken(
        context: Context,
        token: String?,
        tokenRefresh: String?
    ) {
        val editor = getSharedPreference(context).edit()
        editor.putString(KEY_TOKEN, "bearer $token")
        editor.putString(KEY_TOKEN_REFRESH, tokenRefresh)
        editor.apply()
    }

    fun deleteUserSession(context: Context) {
        context.getSharedPreferences(KEY_PREFERENCE, Context.MODE_PRIVATE).edit().clear().apply()
    }
}