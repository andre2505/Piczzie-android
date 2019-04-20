package com.ziggy.kdo.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.model.Gift
import com.ziggy.kdo.network.configuration.Result
import com.ziggy.kdo.repository.GiftRepository
import com.ziggy.kdo.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class HomeViewModel @Inject constructor(var giftRepository: GiftRepository) : BaseViewModel() {

    var listGiftFriends = MutableLiveData<MutableList<Gift>>()

    var mGift = MutableLiveData<Gift>()

    val mResponse = MutableLiveData<Error>()

    init {

        GlobalScope.launch {
            giftRepository.getGiftFriends(0).apply {
                when (this) {
                    is Result.Success -> {
                        listGiftFriends.postValue(this.data as? MutableList<Gift>)
                    }
                    is Result.Error -> {
                        mIsError.postValue(true)
                    }
                    is Result.ErrorNetwork -> {
                        mIsErrorNetwork.postValue(true)
                    }
                }
            }
        }

    }

    fun getOldGifts(offset: Int) {
        GlobalScope.launch {
            giftRepository.getGiftFriends(offset).apply {
                when (this) {
                    is Result.Success -> {
                        listGiftFriends.postValue(this.data as? MutableList<Gift>)
                    }
                    is Result.Error -> {
                        listGiftFriends.postValue(null)
                    }
                    is Result.ErrorNetwork -> {
                        listGiftFriends.postValue(null)
                    }
                }
            }
        }
    }

    fun getYoungerGifts(date: Date?) {
        date?.let {
            GlobalScope.launch {
                giftRepository.getGiftFriendsUpdate(date).apply {
                    when (this) {
                        is Result.Success -> {
                            listGiftFriends.postValue(this.data as? MutableList<Gift>)
                        }
                        is Result.Error -> {
                            listGiftFriends.postValue(null)
                        }
                        is Result.ErrorNetwork -> {
                            listGiftFriends.postValue(null)
                        }
                    }
                }
            }
        }
    }

    fun updateReservedGiftUser(gift: Gift?) {
        gift?.let {
            GlobalScope.launch(Dispatchers.IO) {
                giftRepository.updateGift(gift.id!!, gift).apply {
                    when (this) {
                        is Result.Success -> {
                            mResponse.postValue(Error.NO_ERROR)
                            mGift.postValue(this.data)
                        }
                        is Result.Error -> {
                            mIsError.postValue(true)
                        }
                        is Result.ErrorNetwork -> {
                            mIsErrorNetwork.postValue(true)
                        }
                    }
                }
            }
        }
    }
}
