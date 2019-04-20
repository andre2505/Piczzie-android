package com.ziggy.kdo.network.store

import com.ziggy.kdo.model.Child
import com.ziggy.kdo.model.Gift
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
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
}