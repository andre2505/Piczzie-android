package com.ziggy.kdo.ui.fragment.children


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ziggy.kdo.R
import com.ziggy.kdo.ui.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 *
 */
class AddChildFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_add_child, container, false)
    }


}
