package com.ziggy.kdo.ui.fragment.friends


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ziggy.kdo.R
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.listener.CustomOnItemClickListener
import com.ziggy.kdo.model.User
import com.ziggy.kdo.network.configuration.UserSession
import com.ziggy.kdo.ui.adapter.FriendsAdapter
import com.ziggy.kdo.ui.base.BaseFragment
import com.ziggy.kdo.ui.fragment.profile.ProfileViewModel
import com.ziggy.kdo.ui.fragment.profile.base.ProfileFragment
import com.ziggy.kdo.ui.fragment.profile.base.ProfileFragmentDirections

/**
 * A simple [Fragment] subclass.
 *
 */
class FriendsFragment : BaseFragment(), CustomOnItemClickListener, View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var mProfileViewModel: ProfileViewModel

    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mViewAdapter: RecyclerView.Adapter<*>

    private lateinit var mViewManager: RecyclerView.LayoutManager

    private lateinit var mLinearLayoutManager: LinearLayoutManager

    private lateinit var mSearchFriendsAdapter: FriendsAdapter

    private lateinit var mProgressBar: ProgressBar

    private lateinit var mContentNoFriends: LinearLayout

    private lateinit var mRefreshLayout: SwipeRefreshLayout

    private var mView: View? = null

    private var mDialog: Dialog? = null

    private var mUser: User? = null

    private var mIsOnDeleting = false

    private var mFriendsList: MutableList<User> = mutableListOf()

    private var profileUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.also { activity ->

            profileUser = arguments?.getSerializable(ProfileFragment.ARGS_USER) as? User

            profileUser?.let {
                mProfileViewModel =
                    ViewModelProviders.of(this@FriendsFragment, mViewModeFactory).get(ProfileViewModel::class.java)
                mProfileViewModel.getFriends(profileUser?.id!!)
            } ?: kotlin.run {
                mProfileViewModel = ViewModelProviders.of(activity).get(ProfileViewModel::class.java)
                mProfileViewModel.getFriends(UserSession.getUid(context!!)!!)
            }

            mProfileViewModel.mDeleteFriend.observe(activity, Observer { theSuccess ->
                mDialog?.cancel()
                when (theSuccess) {
                    Error.NO_ERROR -> {
                        mSearchFriendsAdapter.removeFriendList(mUser?.id)
                        mIsOnDeleting = true
                        mFriendsList.remove(mUser)
                        mProfileViewModel.mFriends.value = mFriendsList
                        mProfileViewModel.mDeleteFriend.value = null
                    }
                    Error.ERROR_REQUEST -> {
                        Toast.makeText(activity, R.string.network_error_no_network, Toast.LENGTH_LONG).show()
                        mProfileViewModel.mDeleteFriend.value = null
                    }
                    Error.ERROR_NETWORK -> {
                        Toast.makeText(activity, R.string.network_error_no_network, Toast.LENGTH_LONG).show()
                        mProfileViewModel.mDeleteFriend.value = null
                    }
                    else -> {
                    }
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView?.let {
            return mView
        } ?: kotlin.run {
            mView = inflater.inflate(R.layout.fragment_friends, container, false)

            var deleteFriend = true

            mRecyclerView = mView!!.findViewById(R.id.friends_recycler)
            mProgressBar = mView!!.findViewById(R.id.friends_progressbar)
            mContentNoFriends = mView!!.findViewById(R.id.content_no_friends)
            mRefreshLayout = mView!!.findViewById(R.id.friends_swipe_refresh_layout)

            profileUser?.let {
                deleteFriend = false
            }

            mRefreshLayout.setColorSchemeColors(context?.resources!!.getColor(R.color.colorAccent))
            mRefreshLayout.setOnRefreshListener(this@FriendsFragment)

            mLinearLayoutManager = LinearLayoutManager(activity)

            mViewManager = mLinearLayoutManager

            mRecyclerView.apply {

                setHasFixedSize(true)
                layoutManager = mViewManager

                val dividerItemDecoration = DividerItemDecoration(
                    context,
                    mLinearLayoutManager.orientation
                )
                mRecyclerView.addItemDecoration(dividerItemDecoration)

            }
            activity?.also { activity ->

                mProfileViewModel.mFriends.observe(activity, Observer { theFriends ->
                    mFriendsList = theFriends
                    if (!mIsOnDeleting) {
                        if (!mRefreshLayout.isRefreshing) {
                            mProgressBar.visibility = View.GONE
                            mSearchFriendsAdapter =
                                FriendsAdapter(
                                    theFriends as MutableList<User>,
                                    activity,
                                    this@FriendsFragment,
                                    deleteFriend
                                )
                            mViewAdapter = mSearchFriendsAdapter
                            mRecyclerView.adapter = mViewAdapter
                        } else {
                            mSearchFriendsAdapter.updateList(theFriends)
                            mRefreshLayout.isRefreshing = false
                        }
                    }
                    if (theFriends.isEmpty()) {
                        mContentNoFriends.visibility = View.VISIBLE
                    } else {
                        mContentNoFriends.visibility = View.GONE
                    }
                    mIsOnDeleting = false
                })
            }
        }
        return mView
    }

    override fun <T> onItemClick(view: View?, position: Int?, url: String?, varObject: T?) {
        mUser = varObject as User
        when (view?.id) {
            R.id.list_item_friends_suppress -> {
                getDialogChoiceDeleteFriend()?.show()
            }
            else -> {
                var bundle = bundleOf("user" to mUser)
                Navigation.findNavController(mView!!).navigate(R.id.action_friendsFragment_to_profile2, bundle)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_dialog_yes -> {
                mDialog?.cancel()
                getDialogDeleteFriend()?.show()
                mProfileViewModel.deleteFriend(mUser?.id!!)
            }
            R.id.btn_dialog_no -> {
                mDialog?.cancel()
            }
        }
    }

    override fun onRefresh() {
        mRefreshLayout.isRefreshing = true
        mProfileViewModel.getFriends(UserSession.getUid(context!!)!!)
    }

    private fun getDialogDeleteFriend(): Dialog? {
        activity?.let { theActivity ->
            mDialog = Dialog(theActivity)
            mDialog?.let { theDialog ->
                theDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                theDialog.setCancelable(false)
                theDialog.setContentView(R.layout.view_dialog_reserved)
                val textDelete: TextView = theDialog.findViewById(R.id.view_dialog_reserved_text)
                textDelete.text = getString(R.string.friend_progress_deleting)
                return theDialog
            }
        }
        return null
    }

    private fun getDialogChoiceDeleteFriend(): Dialog? {
        activity?.let { theActivity ->
            mDialog = Dialog(theActivity)
            mDialog?.let { theDialog ->
                theDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                theDialog.setCancelable(false)
                theDialog.setContentView(R.layout.view_dialog_delete_gift)

                val text: TextView = theDialog.findViewById(R.id.text_dialog_sub_title)

                text.text = context?.getString(R.string.friends_dialog_confirm_delete_subtitle)

                val confirmButton: Button = theDialog.findViewById(R.id.btn_dialog_yes)
                val cancelButton: Button = theDialog.findViewById(R.id.btn_dialog_no)

                confirmButton.setOnClickListener(this)
                cancelButton.setOnClickListener(this)

                return theDialog
            }
        }
        return null
    }

}
