package com.ziggy.kdo.ui.fragment.children


import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

/**
 * A simple [Fragment] subclass.
 *
 */
class UpdateChildFragment : BaseFragment(), View.OnClickListener {

    private val mDialog: Dialog by lazy {
        CustomDialog.getDialogLoading(R.string.navigation_children_profile, context)
    }

    private lateinit var mChildViewModel: ChildViewModel

    private lateinit var mUpdateChildBinding: FragmentUpdateChildBinding

    private lateinit var mUpdateButton: Button

    private lateinit var mChildCopy: Child

    private var mView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.also { activity ->
            mChildViewModel = ViewModelProviders.of(activity, mViewModeFactory).get(ChildViewModel::class.java)


            mChildViewModel.mUpdateSuccess.observe(this@UpdateChildFragment, Observer { theSuccess ->
                mDialog.cancel()
                when (theSuccess) {
                    Error.NO_ERROR -> {
                        mChildCopy = mChildViewModel.mChild.value!!
                        activity.supportFragmentManager.popBackStack()
                        mChildViewModel.mUpdateSuccess.value = null
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

            mUpdateButton.setOnClickListener(this@UpdateChildFragment)

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
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if( mUpdateChildBinding.childViewModel?.mChild?.value != mChildCopy){
            mChildViewModel.mChild.value = mChildCopy
        }
    }

}
