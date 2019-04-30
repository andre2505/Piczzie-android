package com.ziggy.kdo.ui.fragment.children


import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation

import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.FragmentUpdateChildBinding
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.model.Child
import com.ziggy.kdo.ui.base.BaseFragment
import com.ziggy.kdo.utils.CustomDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class UpdateChildFragment : BaseFragment(), View.OnClickListener {

    private val cal: Calendar by lazy {
        Calendar.getInstance()
    }

    private val mDialog: Dialog by lazy {
        CustomDialog.getDialogLoading(R.string.update_child_progress, context)
    }

    private val dateSetListener: DatePickerDialog.OnDateSetListener by lazy {
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            mChildViewModel.updateDate(cal.time)
        }
    }


    private lateinit var mChildViewModel: ChildViewModel

    private lateinit var mUpdateChildBinding: FragmentUpdateChildBinding

    private lateinit var mUpdateButton: Button

    private lateinit var mChildCopy: Child

    private lateinit var mEditTextBirthday: EditText

    private var mView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.also { activity ->
            mChildViewModel = ViewModelProviders.of(activity, mViewModeFactory).get(ChildViewModel::class.java)

            mChildViewModel.mUpdateSuccess.observe(activity, Observer { theSuccess ->

                mDialog.cancel()
                when (theSuccess) {
                    Error.NO_ERROR -> {
                        mChildCopy = mChildViewModel.mChild.value!!
                        activity.supportFragmentManager?.popBackStack()
                        mChildViewModel.mUpdateSuccess.value = Error.NOTHING
                    }
                    Error.ERROR_REQUEST -> {
                        Toast.makeText(context, R.string.network_error_no_network, Toast.LENGTH_LONG).show()
                        mChildViewModel.mUpdateSuccess.value = null
                    }
                    Error.ERROR_NETWORK -> {
                        Toast.makeText(context, R.string.network_error_no_network, Toast.LENGTH_LONG).show()
                        mChildViewModel.mUpdateSuccess.value = null
                    }
                    else -> {
                    }
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView?.let {
            return mView
        } ?: kotlin.run {
            mUpdateChildBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_update_child, container, false)
            mView = mUpdateChildBinding.root

            mUpdateButton = mView!!.findViewById(R.id.update_child_button_validate)
            mEditTextBirthday = mView!!.findViewById(R.id.add_child_edittext_birthday)

            mUpdateButton.setOnClickListener(this@UpdateChildFragment)
            mEditTextBirthday.setOnClickListener(this@UpdateChildFragment)

            mUpdateChildBinding.childViewModel = mChildViewModel
            mUpdateChildBinding.lifecycleOwner = this@UpdateChildFragment
            mChildCopy = mChildViewModel.mChild.value!!.copy()

        }
        return mView
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.update_child_button_validate -> {
                mDialog.show()
                mChildViewModel.updateChild()
            }
            R.id.add_child_edittext_birthday -> {
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

    override fun onDestroy() {
        super.onDestroy()

        mChildViewModel.mUpdateSuccess.removeObservers(activity!!)

        if (mChildViewModel.mChild.value != mChildCopy) {
            mChildViewModel.mChild.value = mChildCopy
        }
    }

}
