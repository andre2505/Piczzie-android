package com.ziggy.kdo.ui.fragment.settings.edit_profile.user_information


import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.FragmentUserInformationBinding
import com.ziggy.kdo.enums.Error
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

        mProfileViewModel.mError.observe(this, Observer {
            if (it) {
                showAlertDialogError()
            }
        })


        mProfileViewModel.mEventError.observe(this, Observer { theError ->
            when (theError) {
                Error.ERROR_IS_EMPTY -> Toast.makeText(
                    context,
                    getString(R.string.register_toast_no_valid),
                    Toast.LENGTH_LONG
                )
                    .show()
                else -> {
                }
            }
        })


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

    private fun showAlertDialogError() {

        val dialog = Dialog(activity!!)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.view_dialog_error)

        val dialogButton = dialog.findViewById<Button>(R.id.btn_dialog)
        val dialogButtonRetry = dialog.findViewById<Button>(R.id.btn_dialog_retry)

        dialogButton.setOnClickListener {
            mProfileViewModel.mError.value = false
            dialog.dismiss()
        }

        dialogButtonRetry.setOnClickListener {
            mProfileViewModel.mError.value = false
            dialog.dismiss()
            mProfileViewModel.updateInformationUser()
        }

        dialog.show()
    }


}
