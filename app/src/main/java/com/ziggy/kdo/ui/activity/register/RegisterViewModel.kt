package com.ziggy.kdo.ui.activity.register

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ziggy.kdo.model.User
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.network.configuration.Result
import com.ziggy.kdo.repository.UserRepository
import com.ziggy.kdo.ui.base.BaseViewModel
import com.ziggy.kdo.utils.Validation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class RegisterViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    var mUserRegister = MutableLiveData<User>()

    var mProcessSuccess = MutableLiveData<Boolean>()

    var mValidationSuccess = MutableLiveData<Error>()

    init {
        mUserRegister.value = User()
    }

    fun toProceed() {
        mProcessSuccess.value = (mUserRegister.value?.firstname != null && mUserRegister.value?.firstname != "") &&
                (mUserRegister.value?.lastname != null && mUserRegister.value?.lastname != "") &&
                mUserRegister.value?.gender != null &&
                mUserRegister.value?.birthday != null
    }

    fun toValid() {
        if (TextUtils.isEmpty(mUserRegister.value?.email) || TextUtils.isEmpty(mUserRegister.value?.password)) {
            mValidationSuccess.value = Error.ERROR_IS_EMPTY
        } else if (!Validation.isEmail(mUserRegister.value?.email)) {
            mValidationSuccess.value = Error.ERROR_EMAIL
        } else if (!Validation.isPassword(mUserRegister.value?.password)) {
            mValidationSuccess.value = Error.ERROR_PASSWORD
        } else {
            postUser()
        }
    }

    fun setGender(gender: Int) {
        mUserRegister.value?.gender = gender
    }

    fun updateDate(date: Date) {
        mUserRegister.value?.birthday = date
        mUserRegister.value = mUserRegister.value
    }

    fun postUser() {
        mIsLoading.value = true
        GlobalScope.launch {
            userRepository.postUser(mUserRegister.value!!).apply {
                when (this) {
                    is Result.Success -> {
                        mValidationSuccess.postValue(Error.NO_ERROR)
                    }
                    is Result.Error -> {
                        mValidationSuccess.postValue(Error.ERROR_REQUEST)
                    }
                    is Result.ErrorNetwork -> {
                        mValidationSuccess.postValue(Error.ERROR_REQUEST)
                    }
                }
                mIsLoading.value = false
            }

        }
    }
}