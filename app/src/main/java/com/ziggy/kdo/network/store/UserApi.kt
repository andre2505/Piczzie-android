package com.ziggy.kdo.network.store

import com.ziggy.kdo.model.Token
import com.ziggy.kdo.model.User
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @POST("login")
    fun Authenticaded(@Body user: User): Deferred<Response<Token?>>

    @GET("users")
    fun getUsers(): Deferred<Response<User?>>

    @POST("registration")
    fun postUser(@Body user: User): Deferred<Response<User?>>

    @POST("api/user/token")
    @FormUrlEncoded
    fun postRefreshToken(@Field("token") string: String): Call<Token?>

    @GET("api/user/users")
    fun getSearchUser(@Query("user") search: String): Deferred<Response<List<User>?>>

    @GET("api/user")
    fun getUser(): Deferred<Response<User?>>

    @GET("api/user/friends")
    fun getFriends(): Deferred<Response<List<User>?>>

    @DELETE("api/user/friends/{id}")
    fun deleteFriend(@Path("id") userId: String): Deferred<Response<ResponseBody?>>
}