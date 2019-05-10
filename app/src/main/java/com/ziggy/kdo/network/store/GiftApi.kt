package com.ziggy.kdo.network.store

import com.ziggy.kdo.model.Gift
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import java.util.*


interface GiftApi {
    @Multipart
    @POST("api/gift/create")
    fun upload(
        @Part file: MultipartBody.Part,
        @Part("gift") gift: Gift
    ): Deferred<Response<ResponseBody?>>


    @GET("api/gift/friends")
    fun getGiftFriends(@Query("offset") offset: Int): Deferred<Response<List<Gift>?>>

    @GET("api/gift/friends/update")
    fun getGiftFriendsUpdate(@Query("date") dateGift: Date): Deferred<Response<List<Gift>?>>

    @GET("api/gift/user/{id}")
    fun getGiftsUser(@Path("id") id: String, @Query("offset") offset: Int): Deferred<Response<List<Gift>?>>

    @GET("api/gift/user/reservation")
    fun getGiftsUserReservation(@Query("offset") offset: Int): Deferred<Response<List<Gift>?>>

    @GET("api/gift/child/{id}")
    fun getGiftsChild(@Path("id") id: String): Deferred<Response<MutableList<Gift>?>>

    @PUT("api/gift/update/{id}")
    fun updateGiftFriends(@Path("id") giftId: String, @Body gift: Gift): Deferred<Response<Gift?>>

    @PUT("api/gift/update/{id}/user")
    fun updateGiftUser(@Path("id") giftId: String, @Body gift: Gift): Deferred<Response<Gift?>>

    @DELETE("api/gift/delete/{id}")
    fun deleteGift(@Path("id") giftId: String): Deferred<Response<ResponseBody?>>
}