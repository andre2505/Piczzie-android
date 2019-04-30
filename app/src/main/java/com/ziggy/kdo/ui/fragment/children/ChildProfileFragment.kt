package com.ziggy.kdo.ui.fragment.children


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.FragmentChildProfileBinding
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.listener.CustomOnItemClickListener
import com.ziggy.kdo.model.Child
import com.ziggy.kdo.model.Gift
import com.ziggy.kdo.ui.adapter.GridImageMyGift
import com.ziggy.kdo.ui.base.BaseFragment
import com.ziggy.kdo.ui.fragment.profile.ProfileViewModel
import com.ziggy.kdo.ui.fragment.profile.base.ProfileFragmentDirections
import com.ziggy.kdo.utils.CustomDialog
import com.ziggy.kdo.utils.SpacesItemDecoration

/**
 * A simple [Fragment] subclass.
 *
 */
class ChildProfileFragment : BaseFragment(), CustomOnItemClickListener {

    private val mDialog: Dialog by lazy {
        CustomDialog.getDialogLoading(R.string.delete_child_progress, context)
    }

    private val NUM_GRID_COLUMNS = 3

    private lateinit var mChildViewModel: ChildViewModel

    private lateinit var mChildProfileBinding: FragmentChildProfileBinding

    private lateinit var mGridView: RecyclerView

    private lateinit var mViewManager: RecyclerView.LayoutManager

    private lateinit var mGridLayoutManager: GridLayoutManager

    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    private lateinit var mAdapter: GridImageMyGift

    private lateinit var mProfileViewModel: ProfileViewModel

    private var mVisibleItemCount: Int = 0

    private var mTotalItemCount: Int = 0

    private var mTotalItemCountRequest: Int = 0

    private var mFirstVisibleItem: Int = 0

    private var isScrolling: Boolean = false

    private var mNoMoreLoad: Boolean = false

    private var mView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        exitTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.fade)

        activity?.also { activity ->
            mChildViewModel = ViewModelProviders.of(activity, mViewModeFactory).get(ChildViewModel::class.java)

            mProfileViewModel =
                ViewModelProviders.of(activity, mViewModeFactory).get(ProfileViewModel::class.java)

            //Observe update Gift()
            mProfileViewModel.mUpdateMyGiftSuccess.observe(activity, Observer { theSuccess ->
                when (theSuccess) {
                    Error.NO_ERROR -> {
                        mAdapter.updateGift(mProfileViewModel.mGift.value)
                    }
                    else -> {
                    }
                }
            })

            mProfileViewModel.mDeleteMyGiftSuccess.observe(activity, Observer { theSuccess ->
                when (theSuccess) {
                    Error.NO_ERROR -> {
                        mAdapter.removeGiftList(mProfileViewModel.mGift.value)
                    }
                    else -> {
                    }
                }
            })

            mChildViewModel.mDeleteSuccess.observe(activity, Observer { theSuccess ->

                mDialog.cancel()
                when (theSuccess) {
                    Error.NO_ERROR -> {
                        activity.supportFragmentManager?.popBackStack()
                        mChildViewModel.mUpdateSuccess.value = null
                    }
                    Error.ERROR_REQUEST -> {
                        Toast.makeText(context, R.string.network_error_no_network, Toast.LENGTH_LONG).show()
                        mChildViewModel.mUpdateSuccess.value = null
                    }
                    Error.ERROR_NETWORK -> {
                        Toast.makeText(context, R.string.network_error_no_network, Toast.LENGTH_LONG).show()
                        mChildViewModel.mUpdateSuccess.value = null
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
            mChildProfileBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_child_profile, container, false)
            mView = mChildProfileBinding.root


            mChildProfileBinding.childViewModel = mChildViewModel
            mChildProfileBinding.lifecycleOwner = this@ChildProfileFragment

            mChildViewModel.getGiftChild(mChildViewModel.mChild.value?.id!!)

            mGridView = mView!!.findViewById(R.id.child_profile_recyclerview)

            setupGridView()

        }
        return mView
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.also {activity ->
            mChildViewModel.mDeleteSuccess.removeObservers(activity)
            mProfileViewModel.mUpdateMyGiftSuccess.removeObservers(activity)
            mProfileViewModel.mDeleteMyGiftSuccess.removeObservers(activity)
        }

        mChildViewModel.mChild.value = Child()
        mChildViewModel.mListGiftChild.value = mutableListOf()
    }


    private fun setupGridView() {

        mGridView.apply {

            val gridWitdh = resources.displayMetrics.widthPixels

            val imageWidth = gridWitdh / NUM_GRID_COLUMNS

            val spanCount = 3 // 4 columns

            val spacing = 3 // 50px

            val includeEdge = false

            val dividerItemDecoration = SpacesItemDecoration(spanCount, spacing, includeEdge)

            setHasFixedSize(true)

            mGridLayoutManager = GridLayoutManager(context, 3)

            mGridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (mGridView.adapter?.getItemViewType(position)) {
                        GridImageMyGift.GiftHolder -> 1
                        GridImageMyGift.ProgressHolder -> 3
                        else -> 1
                    }
                }
            }

            mViewManager = mGridLayoutManager

            layoutManager = mViewManager

            addItemDecoration(dividerItemDecoration)

            activity?.also { theActivity ->

                mChildViewModel.mListGiftChild.observe(this@ChildProfileFragment, Observer { theGifts ->
                    if (!::mAdapter.isInitialized) {
                        mAdapter = GridImageMyGift(
                            gifts = theGifts,
                            mSizeImge = imageWidth,
                            customOnItemClick = this@ChildProfileFragment
                        )
                        viewAdapter = mAdapter
                        mGridView.adapter = viewAdapter
                        mTotalItemCountRequest = theGifts.size
                    } else {
                        mAdapter.removeLoading()
                        if (theGifts.size > 0) {
                            mTotalItemCountRequest = theGifts.size
                            mAdapter.updateList(theGifts)
                        } else {
                            mNoMoreLoad = true
                        }
                    }

                })
            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isScrolling = true
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    mVisibleItemCount = childCount
                    mTotalItemCount = layoutManager!!.itemCount
                    mFirstVisibleItem =
                        (recyclerView.layoutManager as GridLayoutManager).findLastVisibleItemPosition()

                    if (!mNoMoreLoad && isScrolling && mTotalItemCount <= (mFirstVisibleItem + mVisibleItemCount)) {
                        isScrolling = false
                        mAdapter.addLoading()
                        mProfileViewModel.getGiftsUser(mTotalItemCount)
                    }
                }

            })

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(mView?.windowToken, 0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_child, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_edit -> {
                exitTransition = null
                Navigation.findNavController(mView!!).navigate(R.id.action_childProfileFragment_to_updateChildFragment)
                activity?.invalidateOptionsMenu()
            }
            R.id.action_delete -> {
                mDialog.show()
                mChildViewModel.deleteChild()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun <T> onItemClick(view: View?, position: Int?, url: String?, varObject: T?) {
        exitTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.fade)

        val gift = varObject as Gift

        val transition = FragmentNavigator.Extras.Builder()
        transition.addSharedElement(view!!, gift.id!!)

        val action: ChildProfileFragmentDirections.ActionChildProfileFragmentToMyGiftDetailFragment =
            ChildProfileFragmentDirections.actionChildProfileFragmentToMyGiftDetailFragment(gift)

        Navigation.findNavController(mView!!).navigate(action, transition.build())
    }

}
