package com.ziggy.kdo.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ziggy.kdo.ui.activity.camera.CameraViewModel
import com.ziggy.kdo.ui.fragment.FragmentViewModel
import dagger.android.AndroidInjection
import dagger.android.DaggerFragment
import dagger.android.support.AndroidSupportInjection

import javax.inject.Inject

/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.03.20
 */
abstract class SubBaseFragment : Fragment() {

    @Inject
    protected lateinit var mViewModeFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        FragmentManager = fragmentManager

    }

    companion object {
        var FragmentManager: FragmentManager? = null
    }

}