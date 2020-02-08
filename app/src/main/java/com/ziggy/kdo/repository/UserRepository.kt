package com.ziggy.kdo.repository

import com.ziggy.kdo.model.Token
import com.ziggy.kdo.network.store.UserApi
import retrofit2.Retrofit
import javax.inject.Inject
import com.ziggy.kdo.model.User
import javax.inject.Singleton
import com.ziggy.kdo.network.configuration.Result
import okhttp3.MultipartBody
import okhttp3.ResponseBody

@Singleton
class UserRepository @Inject constructor(retrofit: Retrofit) : BaseRepository() {

    private var userApi: UserApi = retrofit.create(UserApi::class.java)

    suspend fun postUser(user: User): Result<User> {
        val request = userApi.postUser(user)
        return getResponse(request)
    }

    suspend fun getUsers(): Result<User> {
        val request = userApi.getUsers()
        return getResponse(request)
    }

    suspend fun Authenticated(user: User): Result<User> {
        return getResponse(userApi.Authenticaded(user))
    }

    suspend fun getSearchUser(search: String): Result<List<User>> {
        return getResponse(userApi.getSearchUser(search))
    }

    suspend fun getUser(id: String): Result<User> {
        return getResponse(userApi.getUser(id))
    }

    suspend fun getFriends(id: String): Result<List<User>> {
        return getResponse(userApi.getFriends(id))
    }

    suspend fun deleteFriend(userId: String): Result<ResponseBody> {
        return getResponse(userApi.deleteFriend(userId))
    }

    suspend fun updatefriends(
        userId: String?,
        friendsId: String?,
        state: Int?
    ): Result<List<User>> {
        return getResponse(userApi.updateFriend(userId, friendsId, state))
    }

    suspend fun updatePhoto(userId: String?, multi: MultipartBody.Part): Result<User> {
        return getResponse(userApi.updatePhoto(userId, multi))
    }

    suspend fun updateUserInformations(userId: String?, user: User?): Result<User> {
        return getResponse(userApi.updateUserInformations(userId, user))
    }

}

