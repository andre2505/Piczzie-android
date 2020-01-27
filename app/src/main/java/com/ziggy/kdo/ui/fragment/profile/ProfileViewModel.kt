package com.ziggy.kdo.ui.fragment.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.model.Child
import com.ziggy.kdo.model.Gift
import com.ziggy.kdo.model.User
import com.ziggy.kdo.network.configuration.Result
import com.ziggy.kdo.network.configuration.UserSession
import com.ziggy.kdo.repository.ChildRepository
import com.ziggy.kdo.repository.GiftRepository
import com.ziggy.kdo.repository.UserRepository
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.03.20
 */
class ProfileViewModel @Inject constructor(
    var giftRepository: GiftRepository,
    var userRepository: UserRepository,
    var childRepository: ChildRepository
) : ViewModel() {

    var mLoading = MutableLiveData<Boolean>()

    var mLoadingReserved = MutableLiveData<Boolean>()

    var mListGift = MutableLiveData<MutableList<Gift>>()

    var mListGiftReservation = MutableLiveData<MutableList<Gift>>()

    var mUser = MutableLiveData<User>()

    var mChildren = MutableLiveData<MutableList<Child>>()

    var mError = MutableLiveData<Boolean>()

    var mUpdateMyGiftSuccess = MutableLiveData<Error>()

    var mDeleteMyGiftSuccess = MutableLiveData<Error>()

    var mDeleteFriend = MutableLiveData<Error>()

    var mUpdateGift = MutableLiveData<Boolean>()

    var mGift = MutableLiveData<Gift>()

    var mFriends = MutableLiveData<MutableList<User>>()

    var mStatutFriends = MutableLiveData<Int>()

    val profilePhoto = MutableLiveData<String>()

    lateinit var mCoroutine: Job

    init {
        mUpdateGift.value = false
        mLoading.value = true
        mLoadingReserved.value = false
        mError.value = false
        mGift.value = Gift()
    }

    fun getGiftsUser(offset: Int, userId: String) {

        mCoroutine = GlobalScope.launch(Dispatchers.IO) {

            giftRepository.getGiftsUser(offset, userId).apply {
                when (this) {
                    is Result.Success -> {
                        mListGift.postValue(this.data as MutableList<Gift>)
                    }
                    is Result.Error -> {
                        mError.postValue(true)
                        mCoroutine.cancelAndJoin()
                    }
                    is Result.ErrorNetwork -> {
                        mError.postValue(true)
                        mCoroutine.cancelAndJoin()
                    }
                }
            }

            childRepository.getChildren(userId).apply {
                when (this) {
                    is Result.Success -> {
                        mChildren.postValue(this.data as MutableList<Child>)
                    }
                    is Result.Error -> {
                        mCoroutine.cancelAndJoin()
                        mError.postValue(true)
                    }
                    is Result.ErrorNetwork -> {
                        mCoroutine.cancelAndJoin()
                        mError.postValue(true)
                    }
                }
            }

            userRepository.getUser(userId).apply {
                when (this) {
                    is Result.Success -> {
                        mUser.postValue(this.data)
                        val frienList = this.data?.friends?.filter { month ->
                            month.state == 3
                        }
                        mFriends.postValue(frienList as MutableList<User>)
                        mLoading.postValue(false)
                    }
                    is Result.Error -> {
                        mError.postValue(true)
                        mCoroutine.cancelAndJoin()
                    }
                    is Result.ErrorNetwork -> {
                        mError.postValue(true)
                        mCoroutine.cancelAndJoin()
                    }
                }
            }
        }
    }

    fun getGiftUserReservation(offset: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            giftRepository.getGiftsUserReservation(offset).apply {
                when (this) {
                    is Result.Success -> {
                        mListGiftReservation.postValue(data as MutableList<Gift>)
                    }
                    is Result.Error -> {
                    }
                    is Result.ErrorNetwork -> {
                    }
                }
            }
        }
    }

    fun updateGift() {
        GlobalScope.launch(Dispatchers.IO) {
            giftRepository.updateGiftUser(mGift.value?.id!!, mGift.value!!).apply {
                when (this) {

                    is Result.Success -> {
                        mGift.postValue(this.data as Gift)
                        mUpdateMyGiftSuccess.postValue(Error.NO_ERROR)
                    }
                    is Result.Error -> {
                        mUpdateMyGiftSuccess.postValue(Error.ERROR_REQUEST)
                    }
                    is Result.ErrorNetwork -> {
                        mUpdateMyGiftSuccess.postValue(Error.ERROR_NETWORK)
                    }
                }
            }
        }
    }

    fun deleteGift(id: String?) {
        GlobalScope.launch(Dispatchers.IO) {
            giftRepository.deleteGift(id!!).apply {
                when (this) {
                    is Result.Success -> {
                        mDeleteMyGiftSuccess.postValue(Error.NO_ERROR)
                    }
                    is Result.Error -> {
                        mDeleteMyGiftSuccess.postValue(Error.ERROR_REQUEST)
                    }
                    is Result.ErrorNetwork -> {
                        mDeleteMyGiftSuccess.postValue(Error.ERROR_NETWORK)
                    }
                }
            }
        }
    }


    fun getFriends(userId: String) {
        GlobalScope.launch(Dispatchers.IO) {
            userRepository.getFriends(userId).apply {
                when (this) {
                    is Result.Success -> {
                        mFriends.postValue(this.data as MutableList<User>)
                    }
                    is Result.Error -> {
                        mDeleteFriend.postValue(Error.ERROR_REQUEST)
                    }
                    is Result.ErrorNetwork -> {
                        mDeleteFriend.postValue(Error.ERROR_NETWORK)
                    }
                }
            }
        }
    }

    fun deleteFriend(userId: String) {
        GlobalScope.launch(Dispatchers.IO) {
            userRepository.deleteFriend(userId).apply {
                when (this) {
                    is Result.Success -> {
                        mDeleteFriend.postValue(Error.NO_ERROR)
                    }
                    is Result.Error -> {
                        mDeleteFriend.postValue(Error.ERROR_REQUEST)
                    }
                    is Result.ErrorNetwork -> {
                        mDeleteFriend.postValue(Error.ERROR_NETWORK)
                    }
                }
            }
        }
    }

    fun updateFriend(userId: String?, friendId: String?, state: Int?) {
        GlobalScope.launch(Dispatchers.IO) {
            userRepository.updatefriends(userId, friendId, state).apply {
                when (this) {
                    is Result.Success -> {
                        mStatutFriends.postValue(state)
                        mDeleteFriend.postValue(Error.NO_ERROR)
                    }
                    is Result.Error -> {
                        mDeleteFriend.postValue(Error.ERROR_REQUEST)
                    }
                    is Result.ErrorNetwork -> {
                        mDeleteFriend.postValue(Error.ERROR_NETWORK)
                    }
                }
            }
        }
    }

    fun updatePhoto(userId: String?, multi: MultipartBody.Part) {
        GlobalScope.launch(Dispatchers.IO) {
            userRepository.updatePhoto(userId, multi).apply {
                when (this) {
                    is Result.Success -> {
                        mUser.postValue(this.data)
                        val frienList = this.data?.friends?.filter { month ->
                            month.state == 3
                        }
                        mFriends.postValue(frienList as MutableList<User>)
                        mLoading.postValue(false)
                    }
                    is Result.Error -> {
                        //mError.postValue(true)
                    }
                    is Result.ErrorNetwork -> {
                        //mError.postValue(true)
                    }
                }
            }
        }
    }
}