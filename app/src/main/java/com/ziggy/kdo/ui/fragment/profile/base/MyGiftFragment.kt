package com.ziggy.kdo.ui.fragment.profile.base


import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ziggy.kdo.R
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.listener.CustomOnItemClickListener
import com.ziggy.kdo.model.Gift
import com.ziggy.kdo.ui.adapter.GridImageMyGift
import com.ziggy.kdo.ui.base.BaseFragment
import com.ziggy.kdo.ui.fragment.profile.ProfileViewModel
import com.ziggy.kdo.utils.SpacesItemDecoration


/**
 * A simple [Fragment] subclass.
 *
 */
class MyGiftFragment : BaseFragment(), CustomOnItemClickListener {

    private val NUM_GRID_COLUMNS = 3

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView?.let {
            return mView
        } ?: kotlin.run {
            mView = inflater.inflate(R.layout.fragment_my_gift, container, false)
            mGridView = mView!!.findViewById(R.id.profile_my_gift_recyclerview)
            mProfileViewModel =
                ViewModelProviders.of(activity!!, mViewModeFactory).get(ProfileViewModel::class.java)
            setupGridView()
        }
        return mView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Observe update Gift()
        mProfileViewModel.mUpdateMyGiftSuccess.observe(activity!!, Observer { theSuccess ->
            when (theSuccess) {
                Error.NO_ERROR -> {
                    mAdapter.updateGift(mProfileViewModel.mGift.value)
                }
            }
        })

        //Observe remove Gift()
        mProfileViewModel.mDeleteMyGiftSuccess.observe(activity!!, Observer { theSuccess ->
            when (theSuccess) {
                Error.NO_ERROR -> {
                    mAdapter.removeGiftList(mProfileViewModel.mGift.value)
                }
            }
        })
    }

    override fun <T> onItemClick(view: View?, position: Int?, url: String?, varObject: T?) {


        parentFragment?.exitTransition =
            TransitionInflater.from(parentFragment?.context).inflateTransition(android.R.transition.fade)

        val gift = varObject as Gift

        val transition = FragmentNavigator.Extras.Builder()
        transition.addSharedElement(view!!, gift.id!!)

        val action: ProfileFragmentDirections.ActionProfileToMyGiftDetailFragment =
            ProfileFragmentDirections.actionProfileToMyGiftDetailFragment(gift)
        Navigation.findNavController(mView!!).navigate(action, transition.build())
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

                mProfileViewModel.mListGift.observe(activity!!, Observer { theGifts ->
                    if (!::mAdapter.isInitialized) {
                        mAdapter = GridImageMyGift(
                            gifts = theGifts,
                            mSizeImge = imageWidth,
                            customOnItemClick = this@MyGiftFragment
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
}
