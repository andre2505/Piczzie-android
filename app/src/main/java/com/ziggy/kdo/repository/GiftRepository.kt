package com.ziggy.kdo.repository

import com.ziggy.kdo.model.Child
import com.ziggy.kdo.model.Gift
import com.ziggy.kdo.network.configuration.Result
import com.ziggy.kdo.network.store.GiftApi
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GiftRepository @Inject constructor(private val retrofit: Retrofit) : BaseRepository() {

    val giftApi = retrofit.create(GiftApi::class.java)

    suspend fun postGift(multi: MultipartBody.Part, gift: Gift): Result<ResponseBody> {
        val request = giftApi.upload(file = multi, gift = gift)
        return getResponse(request)
    }

    suspend fun getGiftFriends(offset: Int): Result<List<Gift>> {
        val request = giftApi.getGiftFriends(offset)
        return getResponse(request)
    }

    suspend fun getGiftFriendsUpdate(date: Date): Result<List<Gift>> {
        val request = giftApi.getGiftFriendsUpdate(date)
        return getResponse(request)
    }

    suspend fun updateGift(giftId: String, gift: Gift): Result<Gift> {
        val request = giftApi.updateGiftFriends(giftId, gift)
        return getResponse(request)
    }

    suspend fun updateGiftUser(giftId: String, gift: Gift): Result<Gift> {
        val request = giftApi.updateGiftUser(giftId, gift)
        return getResponse(request)
    }

    suspend fun getGiftsUser(offset: Int):Result<List<Gift>>{
        val request = giftApi.getGiftsUser(offset)
        return getResponse(request)
    }

    suspend fun getGiftsUserReservation(offset: Int):Result<List<Gift>>{
        val request = giftApi.getGiftsUserReservation(offset)
        return getResponse(request)
    }

    suspend fun deleteGift(id: String):Result<ResponseBody>{
        val request = giftApi.deleteGift(id)
        return getResponse(request)
    }

    suspend fun getGiftChild(id: String): Result<MutableList<Gift>>{
        val request = giftApi.getGiftsChild(id)
        return getResponse(request)
    }
}