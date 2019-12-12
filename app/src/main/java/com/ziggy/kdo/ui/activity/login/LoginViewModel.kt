package com.ziggy.kdo.ui.activity.login

import androidx.lifecycle.MutableLiveData
import com.ziggy.kdo.model.Token
import com.ziggy.kdo.model.User
import com.ziggy.kdo.network.configuration.Result
import com.ziggy.kdo.repository.UserRepository
import com.ziggy.kdo.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(var userRepository: UserRepository) : BaseViewModel() {

    var mUserLogin = MutableLiveData<User>()

    var mSucessAuthenticated = MutableLiveData<Boolean>()

    init {
        mUserLogin.value = User()
        mSucessAuthenticated.value = null
        mIsLoading.value = false
    }

    fun login(user: User?) {
        GlobalScope.launch {
            userRepository.Authenticated(user!!).apply {
                when (this) {
                    is Result.Success -> {
                        mUserLogin.postValue(this.data)

                        mSucessAuthenticated.postValue(true)
                    }
                    is Result.Error -> {
                        mSucessAuthenticated.postValue(false)
                    }
                    is Result.ErrorNetwork -> {
                        mSucessAuthenticated.postValue(false)
                    }
                }
            }
        }
    }
}