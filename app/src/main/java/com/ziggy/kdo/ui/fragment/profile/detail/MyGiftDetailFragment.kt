package com.ziggy.kdo.ui.fragment.profile.detail

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.ziggy.kdo.BuildConfig
import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.FragmentMyGiftDetailBinding
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.model.Gift
import com.ziggy.kdo.ui.base.BaseFragment
import com.ziggy.kdo.ui.fragment.profile.ProfileViewModel


/**
 * A simple [Fragment] subclass.
 *
 */
class MyGiftDetailFragment : BaseFragment(), View.OnClickListener {

    private val MY_GIFT = "gift"

    private lateinit var mProfilViewModel: ProfileViewModel

    private lateinit var mMyGiftDetailBinding: FragmentMyGiftDetailBinding

    private lateinit var mImageView: ImageView

    private var mDialog: Dialog? = null

    private var mInitGift: Gift? = null

    private var mBaseGift: Gift? = null

    private var mView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView?.let {
            return mView
        } ?: kotlin.run {

            mMyGiftDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_gift_detail, container, false)

            mView = mMyGiftDetailBinding.root

            mImageView = mView!!.findViewById(R.id.my_gift_image)

            mInitGift = arguments?.getSerializable(MY_GIFT) as Gift

            mBaseGift = mInitGift!!.copy()

            Glide
                .with(this@MyGiftDetailFragment)
                .load(BuildConfig.ENDPOINT + mInitGift?.image)
                .into(mImageView)
        }
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mView?.let {
            mProfilViewModel =
                ViewModelProviders.of(activity!!, mViewModeFactory).get(ProfileViewModel::class.java)
            mMyGiftDetailBinding.profileViewModel = mProfilViewModel
            mMyGiftDetailBinding.lifecycleOwner = this@MyGiftDetailFragment
            mProfilViewModel.mGift.value = mBaseGift

            //Observe update Gift()
            mProfilViewModel.mUpdateMyGiftSuccess.observe(activity!!, Observer { theSuccess ->
                when (theSuccess) {
                    Error.NO_ERROR -> {
                        mDialog?.cancel()
                        mProfilViewModel.mUpdateMyGiftSuccess.value = null
                    }
                    Error.ERROR_REQUEST -> {
                        Toast.makeText(context, R.string.network_error_no_network, Toast.LENGTH_LONG).show()
                    }
                    Error.ERROR_NETWORK -> {
                        Toast.makeText(context, R.string.network_error_no_network, Toast.LENGTH_LONG).show()
                    }
                }
            })

            mProfilViewModel.mDeleteMyGiftSuccess.observe(activity!!, Observer { theSuccess ->
                when (theSuccess) {
                    Error.NO_ERROR -> {
                        mDialog?.cancel()
                        mProfilViewModel.mDeleteMyGiftSuccess.value = null
                        activity!!.supportFragmentManager.popBackStack()
                    }
                    Error.ERROR_REQUEST -> {
                        Toast.makeText(context, R.string.network_error_no_network, Toast.LENGTH_LONG).show()
                    }
                    Error.ERROR_NETWORK -> {
                        Toast.makeText(context, R.string.network_error_no_network, Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        if (mProfilViewModel.mUpdateGift.value == false) {
            inflater?.inflate(R.menu.menu_gift_detail, menu)
        } else {
            inflater?.inflate(R.menu.menu_gift_detail_enabled, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_edit -> {
                mProfilViewModel.mUpdateGift.value = true
                activity?.invalidateOptionsMenu()
            }
            R.id.action_edit_validate -> {
                getDialogUpdateGift()?.show()
                mProfilViewModel.updateGift()
                mProfilViewModel.mUpdateGift.value = false
                activity?.invalidateOptionsMenu()
            }
            R.id.action_abandon -> {
                mProfilViewModel.mGift.value = mInitGift
                mProfilViewModel.mUpdateGift.value = false
                activity?.invalidateOptionsMenu()
            }
            R.id.action_delete -> {
                getDialogChoiceDeleteGift()?.show()
            }
            R.id.action_child -> {
                if(mProfilViewModel.mChildren.value.isNullOrEmpty()){

                }else {

                }
            }
            R.id.action_event -> {

            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_dialog_yes -> {
                mDialog?.cancel()
                getDialogDeleteGift()?.show()
                mProfilViewModel.deleteGift(mInitGift?.id)
            }
            R.id.btn_dialog_no -> {
                mDialog?.cancel()
            }
        }
    }

    private fun getDialogUpdateGift(): Dialog? {
        activity?.let { theActivity ->
            mDialog = Dialog(theActivity)
            mDialog?.let { theDialog ->
                theDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                theDialog.setCancelable(false)
                theDialog.setContentView(R.layout.view_dialog_reserved)
                val textUpdate: TextView = theDialog.findViewById(R.id.view_dialog_reserved_text)
                textUpdate.text = getString(R.string.edit_gift_dialog_update)
                return theDialog
            }
        }
        return null
    }

    private fun getDialogDeleteGift(): Dialog? {
        activity?.let { theActivity ->
            mDialog = Dialog(theActivity)
            mDialog?.let { theDialog ->
                theDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                theDialog.setCancelable(false)
                theDialog.setContentView(R.layout.view_dialog_reserved)
                val textUpdate: TextView = theDialog.findViewById(R.id.view_dialog_reserved_text)
                textUpdate.text = getString(R.string.edit_gift_dialog_delete)
                return theDialog
            }
        }
        return null
    }

    private fun getDialogChoiceDeleteGift(): Dialog? {
        activity?.let { theActivity ->
            mDialog = Dialog(theActivity)
            mDialog?.let { theDialog ->
                theDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                theDialog.setCancelable(false)
                theDialog.setContentView(R.layout.view_dialog_delete_gift)

                val confirmButton: Button = theDialog.findViewById(R.id.btn_dialog_yes)
                val cancelButton: Button = theDialog.findViewById(R.id.btn_dialog_no)

                confirmButton.setOnClickListener(this)
                cancelButton.setOnClickListener(this)

                return theDialog
            }
        }
        return null
    }

    private fun getDialogAddChild(): Dialog? {
        activity?.let { theActivity ->

        }
        return null
    }
}
