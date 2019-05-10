package com.ziggy.kdo.ui.fragment.friends

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.model.User
import com.ziggy.kdo.network.configuration.Result
import com.ziggy.kdo.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class FriendViewModel @Inject constructor(var userRepository: UserRepository) : ViewModel() {

    val mFriends = MutableLiveData<MutableList<User>>()

    val mDeleteFriend = MutableLiveData<Error>()

    fun getFriends(id:String) {
        GlobalScope.launch(Dispatchers.IO) {
            userRepository.getFriends(id).apply {
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

}