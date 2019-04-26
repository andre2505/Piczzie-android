package com.ziggy.kdo.ui.fragment.children


import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.FragmentUpdateChildBinding
import com.ziggy.kdo.ui.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 *
 */
class UpdateChildFragment : BaseFragment() {

    private lateinit var mChildViewModel: ChildViewModel

    private lateinit var mUpdateChildBinding: FragmentUpdateChildBinding

    private var mView: View? = null

    private var mDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.also { activity ->
            mChildViewModel = ViewModelProviders.of(activity, mViewModeFactory).get(ChildViewModel::class.java)
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


            mUpdateChildBinding.childViewModel = mChildViewModel
            mUpdateChildBinding.lifecycleOwner = this@UpdateChildFragment


        }
        return mView
    }


}
