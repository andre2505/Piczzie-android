package com.ziggy.kdo.model

import com.google.gson.annotations.SerializedName

class Token {

    @SerializedName("token")
    var token: String? = null

    @SerializedName("refresh_token")
    var tokenRefresh: String? = null
}