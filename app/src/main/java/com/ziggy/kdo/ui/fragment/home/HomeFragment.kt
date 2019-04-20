package com.ziggy.kdo.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.ziggy.kdo.R
import com.ziggy.kdo.ui.adapter.HomeGiftAdapter
import com.ziggy.kdo.ui.base.BaseFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : BaseFragment(), OnRefreshListener {

    private lateinit var mHomeViewModel: HomeViewModel

    private lateinit var recyclerView: RecyclerView

    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var mLinearLayoutManager: LinearLayoutManager

    private lateinit var mHomeGiftAdapter: HomeGiftAdapter

    private lateinit var mRefreshList: SwipeRefreshLayout

    private var mVisibleItemCount: Int = 0

    private var mTotalItemCount: Int = 0

    private var mFirstVisibleItem: Int = 0

    private var isScrolling: Boolean = false

    private var mLoadMore: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        mRefreshList = view.findViewById(R.id.swipe_refresh_layout)

        recyclerView = view.findViewById(R.id.home_recycler)

        retainInstance = true

        mRefreshList.setOnRefreshListener(this@HomeFragment)

        mRefreshList.setColorSchemeColors(context?.resources!!.getColor(R.color.colorAccent))

        mLinearLayoutManager = LinearLayoutManager(activity)

        viewManager = mLinearLayoutManager

        mRefreshList.isRefreshing = true

        recyclerView.apply {

            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

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
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                    if (mLoadMore && isScrolling && (mTotalItemCount - mVisibleItemCount) <= (mFirstVisibleItem + mVisibleItemCount)) {
                        isScrolling = false
                        mLoadMore = false
                        mHomeGiftAdapter.addLoading()
                        mHomeViewModel.getOldGifts(mTotalItemCount)
                    }
                }
            })
        }

        activity?.let { activity ->
            mHomeViewModel = ViewModelProviders.of(activity).get(HomeViewModel::class.java)

            mHomeViewModel.listGiftFriends.observe(this@HomeFragment, Observer { list ->
                list?.let {
                    if (!::mHomeGiftAdapter.isInitialized) {
                        mRefreshList.isRefreshing = false
                        mHomeGiftAdapter = HomeGiftAdapter(list, activity, mHomeViewModel)
                        viewAdapter = mHomeGiftAdapter
                        recyclerView.adapter = viewAdapter
                    } else {
                        if (!mRefreshList.isRefreshing) {
                            if (list.size > 0) {
                                GlobalScope.launch {
                                    delay(1000)
                                    mHomeGiftAdapter.removeLoading()
                                    mLoadMore = true
                                    mHomeGiftAdapter.addItems(list)
                                }
                            } else {
                                mHomeGiftAdapter.removeLoading()
                                mLoadMore = false
                            }
                        } else {
                            mRefreshList.isRefreshing = false
                            mHomeGiftAdapter.addItemsOnRefresh(list)
                            recyclerView.scrollToPosition(0)
                        }
                    }
                } ?: run {
                    mHomeGiftAdapter.removeLoading()
                    mLoadMore = false
                }
            })

            mHomeViewModel.mGift.observe(activity, Observer { theGift ->
                recyclerView.itemAnimator = null
                mHomeViewModel.mResponse.value = null
                mHomeGiftAdapter.updateItem(theGift)
                theGift.userReserved?.id?.let { theUserReservedId ->
                    if (theUserReservedId != theGift.userRequest) {
                        Toast.makeText(context, R.string.home_reservation_gift_is_reserved, Toast.LENGTH_LONG).show()
                    }
                }
            })

            mHomeViewModel.mIsError.observe(activity, Observer { theError ->
                if (theError) {
                    Toast.makeText(context, R.string.network_error_return, Toast.LENGTH_LONG).show()
                    mHomeViewModel.mIsError.value = false
                }
            })

            mHomeViewModel.mIsErrorNetwork.observe(activity, Observer { theErrorNetwork ->
                if (theErrorNetwork) {
                    Toast.makeText(context, R.string.network_error_no_network, Toast.LENGTH_LONG).show()
                    mHomeViewModel.mIsErrorNetwork.value = false
                }
            })

        }

        return view
    }

    override fun onRefresh() {
        if (mHomeGiftAdapter.itemCount > 0) {
            mRefreshList.isRefreshing = true
            mHomeViewModel.getYoungerGifts(mHomeGiftAdapter.getFirstElement())
        } else {
            mRefreshList.isRefreshing = false
        }

    }
}
