package com.ziggy.kdo.repository

import android.util.Log
import com.ziggy.kdo.network.configuration.Result
import kotlinx.coroutines.Deferred
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

open class BaseRepository {

    private suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>, errorMessage: String): Result<T> =
        try {
            call.invoke()
        } catch (e: Exception) {
            Result.Error(IOException(errorMessage, e))
        }

    suspend fun <T : Any> getResponse(deferred: Deferred<Response<T?>>): Result<T> {
        try {
            val response = deferred.await()
            if (response.isSuccessful) {
                return safeApiCall(call = { Result.Success(response.body()) }, errorMessage = response.code().toString())
            }
            return safeApiCall(
                call = { Result.Error(IOException("TEST 2")) },
                errorMessage = "error"
            )
        } catch (e: Exception) {
            return safeApiCall(
                call = { Result.ErrorNetwork(IOException(e.message)) },
                errorMessage = "error"
            )
        }
    }
}