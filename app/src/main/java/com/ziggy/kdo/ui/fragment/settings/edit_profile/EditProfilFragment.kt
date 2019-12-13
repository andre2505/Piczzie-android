package com.ziggy.kdo.ui.fragment.settings.edit_profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ziggy.kdo.R
import com.ziggy.kdo.ui.adapter.EditProfileAdapter
import com.ziggy.kdo.ui.adapter.HomeGiftAdapter

/**
 * A simple [Fragment] subclass.
 *
 */
class EditProfilFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var editProfileAdapter: EditProfileAdapter

    private lateinit var viewEdit: View

    @StringRes
    private val EDIT_PHOTO = R.string.edit_profile_photo

    @StringRes
    private val EDIT_INFORMATION = R.string.edit_profile_information

    @StringRes
    private val EDIT_EMAIL = R.string.edit_profile_password


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewEdit = inflater.inflate(R.layout.fragment_edit_profil, container, false)

        val arrayEdit = listOf(
            getString(EDIT_PHOTO),
            getString(EDIT_INFORMATION),
            getString(EDIT_EMAIL)
        )

        recyclerView = viewEdit.findViewById(R.id.edit_profile_recyclerview)

        linearLayoutManager = LinearLayoutManager(activity)

        viewManager = linearLayoutManager

        recyclerView.apply {
            setHasFixedSize(true)

            layoutManager = viewManager

            editProfileAdapter = EditProfileAdapter(arrayEdit)
            viewAdapter = editProfileAdapter
            recyclerView.adapter = viewAdapter
        }


        return viewEdit
    }


}
