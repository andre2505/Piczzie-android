package com.ziggy.kdo.ui.fragment.profile.detail


import android.app.Dialog
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.ziggy.kdo.BuildConfig
import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.FragmentMyReservationDetailBinding
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.model.Gift
import com.ziggy.kdo.ui.activity.main.MainActivity
import com.ziggy.kdo.ui.base.BaseFragment
import com.ziggy.kdo.ui.fragment.home.HomeViewModel
import com.ziggy.kdo.ui.fragment.profile.ProfileViewModel
import com.ziggy.kdo.utils.ScrollPositionObserver



/**
 * A simple [Fragment] subclass.
 *
 */
class MyReservationDetailFragment : BaseFragment(), View.OnClickListener {

    private val MY_GIFT = "gift"

    private lateinit var mProfilViewModel: ProfileViewModel

    private lateinit var mHomeViewModel: HomeViewModel

    private lateinit var mMyReservationDetailBinding: FragmentMyReservationDetailBinding

    private lateinit var mImageView: ImageView

    private lateinit var mScrollView: ScrollView

    private var mDialog: Dialog? = null

    private var mInitGift: Gift? = null

    private var mBaseGift: Gift? = null

    private var mView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        enterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView?.let {
            return mView
        } ?: kotlin.run {

            mMyReservationDetailBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_my_reservation_detail, container, false)

            mView = mMyReservationDetailBinding.root

            mImageView = mView!!.findViewById(R.id.my_reservation_image)

            mScrollView = mView!!.findViewById(R.id.reservation_scrollview)

            // Set title bar

            mInitGift = arguments?.getSerializable(MY_GIFT) as Gift

            (activity as MainActivity).supportActionBar?.title = mInitGift?.user?.lastname + " " + mInitGift?.user?.firstname

                mBaseGift = mInitGift!!.copy()

            Glide
                .with(this@MyReservationDetailFragment)
                .load(BuildConfig.ENDPOINT + mInitGift?.image)
                .into(mImageView)


            mScrollView.viewTreeObserver.addOnScrollChangedListener(
                ScrollPositionObserver(
                    mImageView,
                    mScrollView,
                    context!!
                )
            )
        }
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mView?.let {
            mProfilViewModel =
                ViewModelProviders.of(activity!!, mViewModeFactory).get(ProfileViewModel::class.java)

            mHomeViewModel =
                ViewModelProviders.of(activity!!, mViewModeFactory).get(HomeViewModel::class.java)

            mMyReservationDetailBinding.profileViewModel = mProfilViewModel
            mMyReservationDetailBinding.lifecycleOwner = this@MyReservationDetailFragment
            mProfilViewModel.mGift.value = mBaseGift

            mHomeViewModel.mResponse.observe(activity!!, Observer { theResponse ->
                when (theResponse) {
                    Error.NO_ERROR -> {
                        activity?.let {theActivity ->
                            mDialog?.cancel()
                            mHomeViewModel.mResponse.value = null
                            theActivity.supportFragmentManager.popBackStack()
                        }
                    }
                }

            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_reservation_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_favorite -> {
                getDialogChoiceDeleteGift()?.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_dialog_yes -> {
                mDialog?.cancel()
                getDialogDeleteReservation()?.show()
                mHomeViewModel.updateReservedGiftUser(mProfilViewModel.mGift.value)
            }
            R.id.btn_dialog_no -> {
                mDialog?.cancel()
            }
        }
    }


    private fun getDialogChoiceDeleteGift(): Dialog? {
        activity?.let { theActivity ->
            mDialog = Dialog(theActivity)
            mDialog?.let { theDialog ->
                theDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                theDialog.setCancelable(false)
                theDialog.setContentView(R.layout.view_dialog_delete_gift)

                val text: TextView = theDialog.findViewById(R.id.text_dialog_sub_title)

                text.text = context?.getString(R.string.edit_reservation_dialog_confirm_delete_subtitle)

                val confirmButton: Button = theDialog.findViewById(R.id.btn_dialog_yes)
                val cancelButton: Button = theDialog.findViewById(R.id.btn_dialog_no)

                confirmButton.setOnClickListener(this)
                cancelButton.setOnClickListener(this)

                return theDialog
            }
        }
        return null
    }

    private fun getDialogDeleteReservation(): Dialog? {
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
}
