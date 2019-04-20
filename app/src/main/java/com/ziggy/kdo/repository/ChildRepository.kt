package com.ziggy.kdo.repository

import com.ziggy.kdo.model.Child
import com.ziggy.kdo.network.configuration.Result
import com.ziggy.kdo.network.store.ChildApi
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.03.22
 */
class ChildRepository @Inject constructor(retrofit: Retrofit) : BaseRepository() {

    private var childApi: ChildApi = retrofit.create(ChildApi::class.java)

    suspend fun getChildren(): Result<List<Child>> {
        val request = childApi.getChildren()
        return getResponse(request)
    }
}