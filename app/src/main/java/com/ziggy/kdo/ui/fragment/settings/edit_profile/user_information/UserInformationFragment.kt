package com.ziggy.kdo.ui.fragment.settings.edit_profile.user_information


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.FragmentUserInformationBinding
import com.ziggy.kdo.ui.base.BaseFragment
import com.ziggy.kdo.ui.fragment.profile.ProfileViewModel

/**
 * A simple [Fragment] subclass.
 *
 */
class UserInformationFragment : BaseFragment() {

    private lateinit var mProfileViewModel: ProfileViewModel

    private lateinit var mUserInformationBinding: FragmentUserInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { theActivity ->
            mProfileViewModel =
                ViewModelProviders.of(theActivity, mViewModeFactory)
                    .get(ProfileViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mUserInformationBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_information, container, false)
        mUserInformationBinding.profilViewModel = mProfileViewModel
        mUserInformationBinding.lifecycleOwner = this@UserInformationFragment

        return mUserInformationBinding.root
    }


}
