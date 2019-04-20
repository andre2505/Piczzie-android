package com.ziggy.kdo.repository

import com.ziggy.kdo.model.Token
import com.ziggy.kdo.network.store.UserApi
import retrofit2.Retrofit
import javax.inject.Inject
import com.ziggy.kdo.model.User
import javax.inject.Singleton
import com.ziggy.kdo.network.configuration.Result
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

    suspend fun Authenticated(user: User): Result<Token> {
        return getResponse(userApi.Authenticaded(user))
    }

    suspend fun getSearchUser(search: String): Result<List<User>> {
        return getResponse(userApi.getSearchUser(search))
    }

    suspend fun getUser(): Result<User> {
        return getResponse(userApi.getUser())
    }

    suspend fun getFriends(): Result<List<User>> {
        return getResponse(userApi.getFriends())
    }

    suspend fun deleteFriend(userId:String): Result<ResponseBody> {
        return getResponse(userApi.deleteFriend(userId))
    }
}

