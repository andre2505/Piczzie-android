package com.ziggy.kdo.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    var mIsLoading = MutableLiveData<Boolean>()
    var mIsError = MutableLiveData<Boolean>()
    var mIsErrorNetwork = MutableLiveData<Boolean>()

    init {
        mIsLoading.value = false
        mIsError.value = false
        mIsErrorNetwork.value = false
    }
}