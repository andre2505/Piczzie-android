package com.ziggy.kdo.ui.fragment.settings.edit_profile.user_information


import android.app.DatePickerDialog
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
import androidx.navigation.fragment.findNavController

import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.FragmentUserInformationBinding
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.generated.callback.OnClickListener
import com.ziggy.kdo.ui.base.BaseFragment
import com.ziggy.kdo.ui.fragment.profile.ProfileViewModel
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class UserInformationFragment : BaseFragment(), View.OnClickListener {

    private val cal = Calendar.getInstance()

    private lateinit var mProfileViewModel: ProfileViewModel

    private lateinit var mUserInformationBinding: FragmentUserInformationBinding

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            mProfileViewModel.mUser.value?.birthday = cal.time
            mProfileViewModel.mUser.value = mProfileViewModel.mUser.value
        }


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
                Error.NO_ERROR -> {
                    Toast.makeText(
                        context,
                        getString(R.string.edit_profile_update_information),
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().popBackStack()
                    mProfileViewModel.mEventError.value = null
                }
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

        mUserInformationBinding.registerBirthday.setOnClickListener(this@UserInformationFragment)

        return mUserInformationBinding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.register_birthday -> {
                DatePickerDialog(
                    activity!!,
                    R.style.SpinnerDatePickerStyle,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
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
