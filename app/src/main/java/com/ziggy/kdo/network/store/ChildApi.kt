package com.ziggy.kdo.network.store

import com.ziggy.kdo.model.Child
import com.ziggy.kdo.model.Gift
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.03.21
 */
interface ChildApi {

    @GET("api/child/children")
    fun getChildren(): Deferred<Response<List<Child>?>>

    @POST("api/child")
    fun createChild(@Body child: Child): Deferred<Response<ResponseBody?>>
}