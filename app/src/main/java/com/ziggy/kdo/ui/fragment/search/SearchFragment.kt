package com.ziggy.kdo.ui.fragment.search


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ziggy.kdo.R
import com.ziggy.kdo.listener.CustomOnItemClickListener
import com.ziggy.kdo.model.Child
import com.ziggy.kdo.model.User
import com.ziggy.kdo.ui.adapter.SearchFriendsAdapter
import com.ziggy.kdo.ui.base.BaseFragment


/**
 * A simple [Fragment] subclass.
 *
 */
class SearchFragment : BaseFragment(), CustomOnItemClickListener {

    private val ARGS_USER = "user"

    private lateinit var mSearchViewModel: SearchViewModel

    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mViewAdapter: RecyclerView.Adapter<*>

    private lateinit var mViewManager: RecyclerView.LayoutManager

    private lateinit var mLinearLayoutManager: LinearLayoutManager

    private lateinit var mSearchFriendsAdapter: SearchFriendsAdapter

    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mView = inflater.inflate(R.layout.fragment_search, container, false)

        mRecyclerView = mView.findViewById(R.id.search_recycler)

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

            activity?.also { activity ->

                mSearchViewModel = ViewModelProviders.of(activity).get(SearchViewModel::class.java)
                mSearchViewModel.mListUser.observe(activity, Observer { theListUsers ->
                    theListUsers?.let {
                        if (!::mSearchFriendsAdapter.isInitialized) {
                            mSearchFriendsAdapter = SearchFriendsAdapter(theListUsers, activity, this@SearchFragment)
                            mViewAdapter = mSearchFriendsAdapter
                            mRecyclerView.adapter = mViewAdapter
                        } else {
                            mSearchFriendsAdapter.updateUsers(theListUsers)
                        }
                    }
                })

                mSearchViewModel.mIsLoading.observe(activity, Observer { theLoading ->
                    if (theLoading) {
                        if (::mSearchFriendsAdapter.isInitialized) {
                            if (mSearchFriendsAdapter.getLastItem() != null && mSearchFriendsAdapter.getLastItem()?.id != null) {
                                mSearchFriendsAdapter.addLoading()
                            } else {
                                mSearchFriendsAdapter.addLoadingBeginning()
                            }
                        } else {
                            val users = mutableListOf<User>()
                            users.add(User())
                            mSearchFriendsAdapter = SearchFriendsAdapter(users, activity, this@SearchFragment)
                            mViewAdapter = mSearchFriendsAdapter
                            mRecyclerView.adapter = mViewAdapter
                        }
                    }
                })
            }
        }
        return mView
    }

    override fun <T> onItemClick(view: View?, position: Int?, url: String?, varObject: T?) {
        val user = varObject as User

        NavHostFragment.findNavController(this).navigate(R.id.action_search_to_graph_profile, bundleOf(ARGS_USER to user))

    }
}
