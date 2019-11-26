package com.ziggy.kdo.ui.fragment.profile.base


import android.os.Bundle
import android.os.Parcelable
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ziggy.kdo.R
import com.ziggy.kdo.listener.CustomOnItemClickListener
import com.ziggy.kdo.model.Gift
import com.ziggy.kdo.ui.adapter.GridImageMyGift
import com.ziggy.kdo.ui.base.BaseFragment
import com.ziggy.kdo.ui.fragment.home.HomeViewModel
import com.ziggy.kdo.ui.fragment.profile.ProfileViewModel
import com.ziggy.kdo.utils.SpacesItemDecoration


class MyReservationFragment : BaseFragment(), CustomOnItemClickListener {

    private val NUM_GRID_COLUMNS = 3

    private lateinit var mGridView: RecyclerView

    private lateinit var mViewManager: RecyclerView.LayoutManager

    private lateinit var mGridLayoutManager: GridLayoutManager

    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    private lateinit var mAdapter: GridImageMyGift

    private lateinit var mProfileViewModel: ProfileViewModel

    private lateinit var mHomeViewModel: HomeViewModel

    private lateinit var mLoading: ProgressBar

    private var mVisibleItemCount: Int = 0

    private var mTotalItemCount: Int = 0

    private var mTotalItemCountRequest: Int = 0

    private var mFirstVisibleItem: Int = 0

    private var isScrolling: Boolean = false

    private var mNoMoreLoad: Boolean = false

    private var mRestoreList: Parcelable? = null

    private var mView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView?.let { theView ->
            (mView?.parent as ViewGroup).removeView(mView)
        } ?: kotlin.run {
            mView = inflater.inflate(R.layout.fragment_my_reservation, container, false)
            mGridView = mView!!.findViewById(R.id.profile_my_reservation_recyclerview)
            mLoading = mView!!.findViewById(R.id.progress_bar_reservation)
            setupGridView()
        }
        return mView
    }

    override fun <T> onItemClick(view: View?, position: Int?, url: String?, varObject: T?) {

        parentFragment?.exitTransition =
            TransitionInflater.from(parentFragment?.context).inflateTransition(android.R.transition.fade)

        val gift = varObject as Gift

        val transition = FragmentNavigator.Extras.Builder()
        transition.addSharedElement(view!!, gift.id!!)


        val action: ProfileFragmentDirections.ActionProfileToMyReservationDetailFragment =
            ProfileFragmentDirections.actionProfileToMyReservationDetailFragment(gift)
        Navigation.findNavController(mView!!).navigate(action,transition.build())
    }

    private fun setupGridView() {

        mGridView.apply {

            val gridWitdh = resources.displayMetrics.widthPixels

            val imageWidth = gridWitdh / NUM_GRID_COLUMNS

            val spanCount = 3 // 3 columns

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

                mProfileViewModel =
                    ViewModelProviders.of(this@MyReservationFragment, mViewModeFactory)
                        .get(ProfileViewModel::class.java)

                mHomeViewModel =
                    ViewModelProviders.of(theActivity).get(HomeViewModel::class.java)

                mProfileViewModel.getGiftUserReservation(0)

                mProfileViewModel.mListGiftReservation.observe(this@MyReservationFragment, Observer { theGifts ->
                    if (!::mAdapter.isInitialized) {
                        mLoading.visibility = View.GONE
                        mAdapter = GridImageMyGift(
                            gifts = theGifts,
                            mSizeImge = imageWidth,
                            customOnItemClick = this@MyReservationFragment
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

                mHomeViewModel.mGift.observe(theActivity, Observer { theGift ->
                    if (::mAdapter.isInitialized) {
                        theGift.userReserved?.id?.let { theUserReservedId ->
                            if (theUserReservedId == theGift.userRequest) {
                                mAdapter.addGiftList(theGift)
                            }
                        } ?: run {
                            mAdapter.removeGiftList(theGift)
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
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                    if (!mNoMoreLoad && isScrolling && mTotalItemCount <= (mFirstVisibleItem + mVisibleItemCount)) {
                        isScrolling = false
                        mAdapter.addLoading()
                        mProfileViewModel.getGiftUserReservation(mTotalItemCount)
                    }
                }

            })

        }
    }

}
