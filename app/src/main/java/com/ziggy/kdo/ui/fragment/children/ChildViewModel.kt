package com.ziggy.kdo.ui.fragment.children

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ziggy.kdo.model.Child
import com.ziggy.kdo.repository.ChildRepository
import javax.inject.Inject

/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.04.19
 */
class ChildViewModel @Inject constructor(var childRepository: ChildRepository) : ViewModel() {

    val mChild = MutableLiveData<Child>()

    val mChildrenList = MutableLiveData<MutableList<Child>>()

    init {
        mChild.value = Child()
    }

    fun createChild() {

    }

    fun updateChild() {

    }

    fun deleteChild() {

    }

}