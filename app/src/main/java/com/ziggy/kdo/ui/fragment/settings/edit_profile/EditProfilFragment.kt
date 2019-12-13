package com.ziggy.kdo.ui.fragment.settings.edit_profile


import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ziggy.kdo.R
import com.ziggy.kdo.listener.CustomOnItemClickListener
import com.ziggy.kdo.ui.adapter.EditProfileAdapter
import com.ziggy.kdo.ui.adapter.HomeGiftAdapter
import java.util.*
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 *
 */
class EditProfilFragment : Fragment(), CustomOnItemClickListener {

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

        val arrayImage = listOf(
            context?.getDrawable(R.drawable.ic_photo_profile)!!,
            context?.getDrawable(R.drawable.ic_edit_information)!!,
            context?.getDrawable(R.drawable.ic_mail_edit_profile)!!
        )

        recyclerView = viewEdit.findViewById(R.id.edit_profile_recyclerview)

        linearLayoutManager = LinearLayoutManager(activity)

        viewManager = linearLayoutManager

        recyclerView.apply {
            setHasFixedSize(true)

            layoutManager = viewManager

            editProfileAdapter = EditProfileAdapter(arrayEdit, arrayImage, this@EditProfilFragment)
            viewAdapter = editProfileAdapter
            recyclerView.adapter = viewAdapter
        }


        return viewEdit
    }


    override fun <T> onItemClick(view: View?, position: Int?, url: String?, varObject: T?) {
        when (position) {
            0 -> {
                showAlertDialog()
            }
            1 -> {

            }
            2 -> {

            }
        }
    }


    private fun showAlertDialog() {

        val dialog = Dialog(activity!!)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.view_dialog_photo_profil)

        val dialogGallery = dialog.findViewById<Button>(R.id.dialog_choice_gallery)
        val dialogTakePhoto = dialog.findViewById<Button>(R.id.dialog_choice_take_photo)

        dialogGallery.setOnClickListener {
            dialog.dismiss()
        }

        dialogTakePhoto.setOnClickListener {
            dialog.dismiss()
        }



        dialog.show()
    }
}
