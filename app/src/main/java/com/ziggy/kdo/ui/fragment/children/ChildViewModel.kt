package com.ziggy.kdo.ui.fragment.children

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ziggy.kdo.model.Child
import com.ziggy.kdo.repository.ChildRepository
import java.util.*
import javax.inject.Inject
import android.text.method.TextKeyListener.clear
import android.util.Log
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.model.Gift
import com.ziggy.kdo.network.configuration.Result
import com.ziggy.kdo.repository.GiftRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.04.19
 */
class ChildViewModel @Inject constructor(var childRepository: ChildRepository, val giftRepository: GiftRepository) :
    ViewModel() {

    val mChild = MutableLiveData<Child>()

    val mChildrenList = MutableLiveData<MutableList<Child>>()

    val mValidationSuccess = MutableLiveData<Error>()

    val mIsLoading = MutableLiveData<Boolean>()

    val mListGiftChild = MutableLiveData<MutableList<Gift>>()

    init {
        mChild.value = Child()
        mIsLoading.value = true
    }

    fun isChildValid() {
        if (mChild.value?.lastname != null && mChild.value?.firstname != null && mChild.value?.birthday != null && mChild.value?.gender != null) {
            createChild()
        } else {
            mValidationSuccess.value = Error.ERROR_IS_EMPTY
        }
    }

    fun createChild() {
        GlobalScope.launch {
            childRepository.createChild(mChild.value!!).apply {
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
            }

        }
    }

    fun updateChild() {

    }

    fun deleteChild() {

    }

    fun getGiftChild(id: String) {
        GlobalScope.launch(Dispatchers.IO) {
            giftRepository.getGiftChild(id).apply {
                when (this) {
                    is Result.Success -> {
                        mListGiftChild.postValue(this.data)
                        mIsLoading.postValue(false)
                    }
                    is Result.Error -> {
                        mValidationSuccess.postValue(Error.ERROR_REQUEST)
                    }
                    is Result.ErrorNetwork -> {
                        mValidationSuccess.postValue(Error.ERROR_REQUEST)
                    }
                }
            }

        }
    }

    fun updateDate(date: Date) {
        mChild.value?.birthday = date
        mChild.value = mChild.value
    }

    fun setGender(gender: Int) {
        mChild.value?.gender = gender
    }


}