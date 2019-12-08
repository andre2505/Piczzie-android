package com.ziggy.kdo.ui.fragment.profile.base


import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.view.iterator
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.ziggy.kdo.BuildConfig
import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.FragmentProfileBinding
import com.ziggy.kdo.model.User
import com.ziggy.kdo.network.configuration.UserSession
import com.ziggy.kdo.ui.activity.main.MainActivity
import com.ziggy.kdo.ui.base.BaseFragment
import com.ziggy.kdo.ui.fragment.profile.ProfileViewModel


/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : BaseFragment(), TabLayout.OnTabSelectedListener, View.OnClickListener {

    private val TAG_MY_GIFT = MyGiftFragment::class.java.simpleName

    private val TAG_MY_RESERVATION = MyReservationFragment::class.java.simpleName

    private val ARGS_USER = "user"

    private val FRIEND_ADD: Int = 0

    private val FRIEND_WAIT: Int = 1

    private val FRIEND_ACCEPT: Int = 2

    private val FRIEND_REFUSE: Int = 4

    private lateinit var mProfileViewModel: ProfileViewModel

    private lateinit var mTab: TabLayout

    private lateinit var mFragmentMyGiftFragment: MyGiftFragment

    private lateinit var mPhotoProfil: ImageView

    private lateinit var mProfileBinding: FragmentProfileBinding

    private lateinit var mButtonFriends: Button

    private lateinit var mButtonChild: Button

    private lateinit var buttonAddFriend: Button

    private var mFragmentMyReservationFragment: MyReservationFragment? = null

    private var isInit = false

    private var mView: View? = null

    private var mUser: User? = null

    private lateinit var mUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mUser = arguments?.getSerializable(ARGS_USER) as? User
        val args = Bundle()
        mUser?.let {
            setHasOptionsMenu(true)
            val act = (activity as MainActivity)
            act.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

            args.putSerializable(ARGS_USER, mUser)
            mProfileViewModel =
                ViewModelProviders.of(this@ProfileFragment, mViewModeFactory).get(ProfileViewModel::class.java)
            mUserId = mUser?.id!!
        } ?: kotlin.run {

            args.putSerializable(ARGS_USER, null)
            mProfileViewModel =
                ViewModelProviders.of(activity!!, mViewModeFactory).get(ProfileViewModel::class.java)
            mUserId = UserSession.getUid(context!!)!!
        }

        mFragmentMyGiftFragment = MyGiftFragment()
        mFragmentMyGiftFragment.arguments = args
        mProfileViewModel.getGiftsUser(0, mUserId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView?.let {
            return mView
        } ?: kotlin.run {
            mProfileBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

            mView = mProfileBinding.root

            mTab = mView!!.findViewById(R.id.profile_tab)

            //Image
            mPhotoProfil = mView!!.findViewById(R.id.profile_image)

            //Button
            mButtonFriends = mView!!.findViewById(R.id.profile_number_friends)
            mButtonChild = mView!!.findViewById(R.id.profile_number_children)
            buttonAddFriend = mView!!.findViewById(R.id.button_add_friend)

            //Clicklistener
            mButtonFriends.setOnClickListener(this@ProfileFragment)
            mButtonChild.setOnClickListener(this@ProfileFragment)
            buttonAddFriend.setOnClickListener(this@ProfileFragment)

            mProfileBinding.profileViewModel = mProfileViewModel
            mProfileBinding.lifecycleOwner = this@ProfileFragment

            mProfileViewModel.mUser.observe(this@ProfileFragment, Observer { theUser ->

                val thumbnailGender: Int = if (theUser.gender == 1) {
                    R.drawable.ic_profile_man
                } else {
                    R.drawable.ic_profile_woman
                }

                theUser?.let {
                    Glide
                        .with(mPhotoProfil)
                        .load(BuildConfig.ENDPOINT + theUser.photo)
                        .apply(
                            RequestOptions
                                .circleCropTransform()
                        )
                        .thumbnail(Glide.with(mPhotoProfil).load(thumbnailGender))
                        .into(mPhotoProfil)

                    //uid in list friend
                    run loop@{
                        theUser.friends?.forEach { theFriends ->
                            if (theFriends.friendID == UserSession.getUid(context!!)) {
                                mProfileViewModel.mStatutFriends.value = theFriends.state
                                return@loop
                            }
                        }
                    }
                }
            })


            //configure first child
            mTab.addOnTabSelectedListener(this@ProfileFragment)
            mTab.getTabAt(0)?.icon?.setColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN)
            if (!isInit) {
                val ft = childFragmentManager.beginTransaction()
                ft.add(R.id.profile_container_recycler, mFragmentMyGiftFragment, TAG_MY_GIFT)
                ft.commit()
            } else {
                val ft = childFragmentManager.beginTransaction()
                ft.show(childFragmentManager.findFragmentByTag(TAG_MY_GIFT)!!)
                ft.commit()
            }
            isInit = true
        }
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mUser?.let {
            (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu?.size()!! > 0) {
            mUser?.let {
                menu.getItem(0)?.isVisible = false
            } ?: kotlin.run {
                menu.getItem(0)?.isVisible = true
            }
        }
    }

    override fun onTabReselected(p0: TabLayout.Tab?) {

    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
        p0?.icon?.setColorFilter(resources.getColor(R.color.colorGrayShadow), PorterDuff.Mode.SRC_IN)
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        p0?.icon?.setColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN)
        val ft = childFragmentManager.beginTransaction()
        when (p0?.position) {
            0 -> {
                ft.hide(childFragmentManager.findFragmentByTag(TAG_MY_RESERVATION)!!)
                ft.show(childFragmentManager.findFragmentByTag(TAG_MY_GIFT)!!)
                ft.commit()
            }
            1 -> {
                ft.hide(childFragmentManager.findFragmentByTag(TAG_MY_GIFT)!!)
                mFragmentMyReservationFragment?.let { theFragmentReservation ->
                    ft.show(childFragmentManager.findFragmentByTag(TAG_MY_RESERVATION)!!)
                    ft.commit()
                } ?: kotlin.run {
                    mFragmentMyReservationFragment = MyReservationFragment()
                    ft.add(R.id.profile_container_recycler, mFragmentMyReservationFragment!!, TAG_MY_RESERVATION)
                    ft.commit()
                }
            }
        }
    }

    override fun onClick(v: View?) {

        exitTransition = null

        when (v?.id) {
            R.id.profile_number_friends -> {
                mUser?.let {
                    Navigation.findNavController(mView!!)
                        .navigate(R.id.action_profile_to_friendsFragment, bundleOf(ARGS_USER to mUser))
                } ?: kotlin.run {
                    Navigation.findNavController(mView!!).navigate(R.id.action_profile_to_friendsFragment)
                }
            }
            R.id.profile_number_children -> {

                mUser?.let { theUser ->
                    Navigation.findNavController(mView!!)
                        .navigate(R.id.action_profile_to_childrenFragment, bundleOf(ARGS_USER to theUser))
                } ?: kotlin.run {
                    Navigation.findNavController(mView!!).navigate(R.id.action_profile_to_childrenFragment)
                }
            }
            R.id.button_add_friend -> {
                when (mProfileViewModel.mStatutFriends.value) {
                    FRIEND_ADD -> {
                        updateStateFriends(FRIEND_REFUSE)
                    }
                    FRIEND_WAIT -> {
                        updateStateFriends(FRIEND_ACCEPT)
                    }
                    FRIEND_ACCEPT -> {
                        updateStateFriends(FRIEND_REFUSE)
                    }
                    else -> {
                        updateStateFriends(FRIEND_ADD)
                    }
                }
            }
            R.id.button_refuse_friend -> {
                updateStateFriends(FRIEND_REFUSE)
            }
        }
    }

    private fun updateStateFriends(state: Int?) {
        mUser?.let {
            mProfileViewModel.updateFriend(
                mUser?.id!!,
                UserSession.getUid(context!!)!!,
                state
            )
        }
    }

    companion object {
        val ARGS_USER = "user"
    }
}
