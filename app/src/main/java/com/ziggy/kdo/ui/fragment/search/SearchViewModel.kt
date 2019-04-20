package com.ziggy.kdo.ui.fragment.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ziggy.kdo.model.Gift
import com.ziggy.kdo.model.User
import com.ziggy.kdo.network.configuration.Result
import com.ziggy.kdo.repository.UserRepository
import com.ziggy.kdo.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.03.12
 */
class SearchViewModel @Inject constructor(var userRepository: UserRepository) : BaseViewModel() {

    var mListUser = MutableLiveData<MutableList<User>>()

    fun getSearchUser(search: String) {

        mIsLoading.value = true

        GlobalScope.launch {
            userRepository.getSearchUser(search).apply {
                when (this) {
                    is Result.Success -> {
                        mListUser.postValue(this.data as? MutableList<User>)
                        mIsLoading.postValue(false)
                    }
                    is Result.Error -> {
                        mIsError.postValue(true)
                        mIsLoading.postValue(false)
                    }
                    is Result.ErrorNetwork -> {
                        mIsErrorNetwork.postValue(true)
                        mIsLoading.postValue(false)
                    }
                }
            }
        }
    }
}