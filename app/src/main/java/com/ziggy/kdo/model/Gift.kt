package com.ziggy.kdo.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.ziggy.kdo.BR
import java.io.Serializable
import java.util.*

data class Gift(
    @SerializedName("_id")
    var id: String? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("price")
    var price: Double? = null,

    @SerializedName("place")
    var place: String? = null,

    @SerializedName("website")
    var website: String? = null,

    @SerializedName("date")
    var date: Date? = null,

    @SerializedName("image")
    var image: String? = null,

    @SerializedName("user_id")
    var user: User? = null,

    var _userReserved: User? = null,

    @SerializedName("user_request")
    var userRequest: String? = null
) : BaseObservable(), Serializable {

    @SerializedName("user_reserved_id")
    @get:Bindable
    var userReserved= _userReserved
        set (value) {
            field = value
            notifyPropertyChanged(BR.userReserved)
        }

}
