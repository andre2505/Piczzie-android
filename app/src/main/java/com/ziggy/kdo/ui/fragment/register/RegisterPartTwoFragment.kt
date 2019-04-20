package com.ziggy.kdo.ui.fragment.register

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.FragmentRegisterPartTwoBinding
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.ui.activity.register.RegisterViewModel


class RegisterPartTwoFragment : Fragment() {

    private lateinit var mRegisterViewModel: RegisterViewModel

    private lateinit var mRegisterPartTwoBinding: FragmentRegisterPartTwoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mRegisterPartTwoBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_register_part_two, container, false)

        //view?.findViewById<TextView>(R.id.register_user_conditions)?.movementMethod = LinkMovementMethod.getInstance()

        return mRegisterPartTwoBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mRegisterViewModel = ViewModelProviders.of(activity!!).get(RegisterViewModel::class.java)

        mRegisterPartTwoBinding.registerViewModel = mRegisterViewModel
        mRegisterPartTwoBinding.setLifecycleOwner(activity!!)

        mRegisterViewModel.mValidationSuccess.observe(activity!!, Observer { error ->
            when (error!!) {
                Error.ERROR_IS_EMPTY -> {
                    Toast.makeText(activity!!, getString(R.string.register_toast_no_valid), Toast.LENGTH_LONG).show()
                }
                Error.ERROR_EMAIL -> {
                    Toast.makeText(activity!!, getString(R.string.register_toast_error_email), Toast.LENGTH_LONG).show()
                }
                Error.ERROR_PASSWORD -> {
                    Toast.makeText(activity!!, getString(R.string.register_toast_error_password), Toast.LENGTH_LONG)
                        .show()
                }
                Error.NO_ERROR -> {
                    showAlertDialog()
                }
                Error.ERROR_REQUEST -> {
                    showAlertDialogError()
                }
            }
        })
    }

    private fun showAlertDialog() {

        val dialog = Dialog(activity!!)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.view_dialog_validation)
        val dialogButton = dialog.findViewById<Button>(R.id.btn_dialog)

        dialogButton.setOnClickListener {
            dialog.dismiss()
            activity!!.finish()
        }

        dialog.show()
    }

    private fun showAlertDialogError() {

        val dialog = Dialog(activity!!)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.view_dialog_error)

        val dialogButton = dialog.findViewById<Button>(R.id.btn_dialog)
        val dialogButtonRetry = dialog.findViewById<Button>(R.id.btn_dialog_retry)

        dialogButton.setOnClickListener {
            dialog.dismiss()
            activity!!.finish()
        }

        dialogButtonRetry.setOnClickListener {
            dialog.dismiss()
            mRegisterViewModel.postUser()
        }

        dialog.show()
    }
}