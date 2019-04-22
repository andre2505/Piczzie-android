package com.ziggy.kdo.ui.fragment.children


import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.FragmentAddChildBinding
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

    private var mView: View? = null

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
            mEditTextDate.setOnClickListener(this@AddChildFragment)

            activity?.also { activity ->
                mChildViewModel = ViewModelProviders.of(activity, mViewModeFactory).get(ChildViewModel::class.java)

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
        }
    }
}
