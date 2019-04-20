package com.ziggy.kdo.ui.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.04.07
 */
class FragmentViewModel: ViewModel() {

    var currentFragment = MutableLiveData<Fragment>()
}