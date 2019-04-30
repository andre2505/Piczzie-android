package com.ziggy.kdo.ui.fragment.children


import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.FragmentAddChildBinding
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.model.Child
import com.ziggy.kdo.ui.base.BaseFragment
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
class AddChildFragment : BaseFragment(), View.OnClickListener {

    private val cal = Calendar.getInstance()

    private val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        mChildViewModel.updateDate(cal.time)
    }

    private lateinit var mChildViewModel: ChildViewModel

    private lateinit var mChildFragmentBinding: FragmentAddChildBinding

    private lateinit var mEditTextDate: EditText

    private lateinit var mButtonAddChild: Button

    private var mView: View? = null

    private var mListChildren: MutableList<Child>? = null

    private var mDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mView?.let {
            return mView
        } ?: kotlin.run {
            mChildFragmentBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_add_child, container, false)

            mView = mChildFragmentBinding.root

            mEditTextDate = mView!!.findViewById(R.id.add_child_edittext_birthday)
            mButtonAddChild = mView!!.findViewById(R.id.add_child_button_validate)

            mEditTextDate.setOnClickListener(this@AddChildFragment)
            mButtonAddChild.setOnClickListener(this@AddChildFragment)

            activity?.also { activity ->
                mChildViewModel = ViewModelProviders.of(activity, mViewModeFactory).get(ChildViewModel::class.java)

                mChildViewModel.mValidationSuccess.observe(
                    this@AddChildFragment, Observer { theSuccess ->
                        mDialog?.cancel()
                        theSuccess?.also {
                            when (theSuccess) {
                                Error.NO_ERROR -> {
                                    mListChildren = mChildViewModel.mChildrenList.value
                                    mListChildren?.add(mChildViewModel.mChild.value!!)
                                    mChildViewModel.mChildrenList.value = mListChildren
                                    activity.supportFragmentManager.popBackStack()
                                }
                                Error.ERROR_IS_EMPTY -> {
                                    Toast.makeText(
                                        activity,
                                        getString(R.string.add_child_empty_fields),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                else -> {
                                    Toast.makeText(context, R.string.network_error_return, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    })

                mChildFragmentBinding.childViewModel = mChildViewModel

                mChildFragmentBinding.lifecycleOwner = this@AddChildFragment
            }
        }

        return mView
    }

    override fun onClick(v: View?) {
        when (v?.id) {
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
            R.id.add_child_button_validate -> {
                getDialogAddChild()?.show()
                mChildViewModel.isChildValid()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mChildViewModel.mValidationSuccess.value = null
        mChildViewModel.mChild.value = Child()
    }

    private fun getDialogAddChild(): Dialog? {
        activity?.let { theActivity ->
            mDialog = Dialog(theActivity)
            mDialog?.let { theDialog ->
                theDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                theDialog.setCancelable(false)
                theDialog.setContentView(R.layout.view_dialog_reserved)
                val textDelete: TextView = theDialog.findViewById(R.id.view_dialog_reserved_text)
                textDelete.text = getString(R.string.dialog_add)
                return theDialog
            }
        }
        return null
    }
}
