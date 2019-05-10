package com.ziggy.kdo.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class User: Serializable {

    @SerializedName("_id")
    var id: String? = null

    @SerializedName("firstname")
    var firstname: String? = null

    @SerializedName("lastname")
    var lastname: String? = null

    @SerializedName("gender")
    var gender: Int? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("birthday")
    var birthday: Date? = null

    @SerializedName("photo")
    var photo: String? = null

    @SerializedName("friends")
    var friends: List<User>? = null

    @SerializedName("state")
    var state: Int? = null
}