package com.ziggy.kdo.utils

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern

object Validation {
    fun isEmail(email: String?): Boolean {
        if (email != null && email != "") {
            return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())
        }
        return false
    }

    fun isPassword(password: String?): Boolean {
        val regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$"
        if (!TextUtils.isEmpty(password)) {
            return  Pattern.compile(regex).matcher(password).matches()
        }
        return false
    }
}