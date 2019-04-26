package com.ziggy.kdo.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.03.21
 */
data class Child (

    @SerializedName("_id")
    var id: String? = null,

    @SerializedName("firstname")
    var firstname: String? = null,

    @SerializedName("lastname")
    var lastname: String? = null,

    @SerializedName("gender")
    var gender: Int? = null,

    @SerializedName("birthday")
    var birthday: Date? = null,

    @SerializedName("parent")
    var parent: User? = null
)
