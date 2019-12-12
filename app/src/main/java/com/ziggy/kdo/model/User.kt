package com.ziggy.kdo.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class User(
    @SerializedName("_id")
    var id: String? = null,

    @SerializedName("firstname")
    var firstname: String? = null,

    @SerializedName("lastname")
    var lastname: String? = null,

    @SerializedName("gender")
    var gender: Int? = null,

    @SerializedName("email")
    var email: String? = null,

    @SerializedName("password")
    var password: String? = null,

    @SerializedName("birthday")
    var birthday: Date? = null,

    @SerializedName("photo")
    var photo: String? = null,

    @SerializedName("friends")
    var friends: List<User>? = null,

    @SerializedName("state")
    var state: Int? = null,

    @SerializedName("friends_id")
    var friendID: String? = null,

    @SerializedName("token")
    var token: String? = null,

    @SerializedName("refresh_token")
    var tokenRefresh: String? = null

) : Serializable, Parcelable {
    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        firstname = parcel.readString()
        lastname = parcel.readString()
        gender = parcel.readValue(Int::class.java.classLoader) as? Int
        email = parcel.readString()
        password = parcel.readString()
        photo = parcel.readString()
        friends = parcel.createTypedArrayList(CREATOR)
        state = parcel.readValue(Int::class.java.classLoader) as? Int
        friendID = parcel.readString()
        token = parcel.readString()
        tokenRefresh = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(firstname)
        parcel.writeString(lastname)
        parcel.writeValue(gender)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(photo)
        parcel.writeTypedList(friends)
        parcel.writeValue(state)
        parcel.writeString(friendID)
        parcel.writeString(token)
        parcel.writeString(tokenRefresh)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}