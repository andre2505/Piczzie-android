package com.ziggy.kdo.ui.fragment.children


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.FragmentAddChildBinding
import com.ziggy.kdo.ui.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 *
 */
class AddChildFragment : BaseFragment() {

    private lateinit var mChildViewModel: ChildViewModel

    private lateinit var mChildFragmentBinding: FragmentAddChildBinding

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
        }

        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.also { activity ->
            mChildViewModel = ViewModelProviders.of(activity, mViewModeFactory).get(ChildViewModel::class.java)

            mChildFragmentBinding.childViewModel = mChildViewModel
            mChildFragmentBinding.lifecycleOwner =activity
        }
    }

}
