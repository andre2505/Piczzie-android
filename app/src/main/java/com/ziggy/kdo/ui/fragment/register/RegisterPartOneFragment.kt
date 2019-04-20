package com.ziggy.kdo.ui.fragment.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.FragmentRegisterPartOneBinding
import com.ziggy.kdo.ui.activity.register.RegisterViewModel
import java.util.*

class RegisterPartOneFragment : Fragment(), View.OnClickListener {

    private var cal = Calendar.getInstance()

    lateinit var mRegisterViewModel: RegisterViewModel

    lateinit var mRegisterPartOneBinding: FragmentRegisterPartOneBinding

    lateinit var mButtonProceed: Button

    lateinit var mEditTextDate: EditText

    private val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        mRegisterViewModel.updateDate(cal.time)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        mRegisterPartOneBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_register_part_one, container, false)

        val view = mRegisterPartOneBinding.root

        //create alerdialog date
        mEditTextDate = view.findViewById(R.id.register_birthday)
        mEditTextDate.setOnClickListener(this)

        //button next
        mButtonProceed = view.findViewById(R.id.button_proceed)
        mButtonProceed.setOnClickListener(this)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        mRegisterViewModel = ViewModelProviders.of(activity!!).get(RegisterViewModel::class.java)

        mRegisterPartOneBinding.registerViewModel = mRegisterViewModel

        mRegisterPartOneBinding.setLifecycleOwner(activity!!)

        mRegisterViewModel.mProcessSuccess.observe(activity!!, Observer {
            when (it) {
                true -> {
                    Navigation.findNavController(mButtonProceed).navigate(R.id.to_register_part_two)
                    mRegisterViewModel.mProcessSuccess.value = null
                }
                false -> {
                    Toast.makeText(activity!!, getString(R.string.register_toast_no_valid), Toast.LENGTH_LONG).show()
                }
            }
        })
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


}