package com.ziggy.kdo.ui.activity.camera


import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.model.Gift
import com.ziggy.kdo.model.User
import com.ziggy.kdo.network.configuration.Result
import com.ziggy.kdo.repository.GiftRepository
import com.ziggy.kdo.repository.GiftRepository_Factory
import com.ziggy.kdo.repository.UserRepository
import com.ziggy.kdo.ui.base.BaseViewModel
import com.ziggy.kdo.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import javax.inject.Inject
import okhttp3.RequestBody
import java.util.*


/**
 * MainViewModel
 */
class CameraViewModel @Inject constructor(private val mGiftRepository: GiftRepository) : BaseViewModel() {

    //get repository
    var mGift = MutableLiveData<Gift>()

    var mValidationSuccess = MutableLiveData<Error>()

    init {
        mGift.value = Gift()
        mGift.value?.date = Date()
    }

    fun addGift() {
        if (TextUtils.isEmpty(mGift.value?.description)) {
            mValidationSuccess.value = Error.ERROR_EMPTY_DESCRIPTION
        } else {
            mValidationSuccess.value = Error.NO_ERROR
        }
    }
}