package com.ziggy.kdo.ui.activity.main

import androidx.lifecycle.MutableLiveData
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.model.Gift
import com.ziggy.kdo.model.User
import com.ziggy.kdo.network.configuration.Result
import com.ziggy.kdo.repository.GiftRepository
import com.ziggy.kdo.repository.UserRepository
import com.ziggy.kdo.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * MainViewModel
 */
class MainViewModel @Inject constructor(
    private val mUserRepository: UserRepository,
    private val mGiftRepository: GiftRepository
) : BaseViewModel() {

    //get repository
    var mUser = MutableLiveData<User>()

    var error = MutableLiveData<Boolean>()

    var mMultipartBody = MutableLiveData<MultipartBody.Part>()

    var mGift = MutableLiveData<Gift>()

    var mValidationSuccess = MutableLiveData<Error>()

    init {
        error.value = false
    }

    private fun initUser() {
        GlobalScope.launch {
            mUserRepository.getUsers().apply {
                when (this) {
                    is Result.Success -> {
                        mUser.postValue(this.data)
                    }
                    is Result.Error -> {
                        error.postValue(true)
                    }
                }
            }

        }
    }

    fun uploadGift() {
        GlobalScope.launch {
            mGiftRepository.postGift(mMultipartBody.value!!, mGift.value!!).apply {
                when (this) {
                    is Result.Success -> {
                        mValidationSuccess.postValue(Error.NO_ERROR)
                    }
                    is Result.Error -> {
                        mValidationSuccess.postValue(Error.ERROR_REQUEST)
                    }
                    is Result.ErrorNetwork -> {
                        mValidationSuccess.postValue(Error.ERROR_NETWORK)
                    }
                }
            }

        }
    }
}