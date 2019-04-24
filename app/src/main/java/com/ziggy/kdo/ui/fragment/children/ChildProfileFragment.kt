package com.ziggy.kdo.ui.fragment.children


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.FragmentChildProfileBinding
import com.ziggy.kdo.model.Child
import com.ziggy.kdo.ui.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 *
 */
class ChildProfileFragment : BaseFragment() {

    private lateinit var mChildViewModel: ChildViewModel

    private var mView: View? = null

    private lateinit var mChildProfileBinding: FragmentChildProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

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
            mChildProfileBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_child_profile, container, false)
            mView = mChildProfileBinding.root


            mChildProfileBinding.childViewModel = mChildViewModel
            mChildProfileBinding.lifecycleOwner = this@ChildProfileFragment

            mChildViewModel.getGiftChild(mChildViewModel.mChild.value?.id!!)

        }
        return mView
    }

    override fun onDestroy() {
        super.onDestroy()
        mChildViewModel.mChild.value = Child()
    }

}
