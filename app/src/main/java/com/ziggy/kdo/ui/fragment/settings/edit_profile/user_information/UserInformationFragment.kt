package com.ziggy.kdo.ui.fragment.settings.edit_profile.user_information


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ziggy.kdo.R

/**
 * A simple [Fragment] subclass.
 *
 */
class UserInformationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_information, container, false)
    }


}
