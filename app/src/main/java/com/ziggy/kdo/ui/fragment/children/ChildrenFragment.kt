package com.ziggy.kdo.ui.fragment.children


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ziggy.kdo.R
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.listener.CustomOnItemClickListener
import com.ziggy.kdo.model.Child
import com.ziggy.kdo.model.User
import com.ziggy.kdo.ui.adapter.ChildrenAdapter
import com.ziggy.kdo.ui.base.BaseFragment
import com.ziggy.kdo.ui.fragment.profile.ProfileViewModel

/**
 * A simple [Fragment] subclass.
 *
 */
class ChildrenFragment : BaseFragment(), CustomOnItemClickListener, View.OnClickListener {

    private lateinit var mProfileViewModel: ProfileViewModel

    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mViewAdapter: RecyclerView.Adapter<*>

    private lateinit var mViewManager: RecyclerView.LayoutManager

    private lateinit var mLinearLayoutManager: LinearLayoutManager

    private lateinit var mChildrenAdapter: ChildrenAdapter

    private lateinit var mProgressBar: ProgressBar

    private lateinit var mContentNoChildren: LinearLayout

    private var mView: View? = null

    private var mDialog: Dialog? = null

    private var mUser: User? = null

    private var mChildrenList: MutableList<Child> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView?.let {
            return mView
        } ?: kotlin.run {
            mView = inflater.inflate(R.layout.fragment_children, container, false)

            mRecyclerView = mView!!.findViewById(R.id.children_recycler)
            mProgressBar = mView!!.findViewById(R.id.children_progressbar)
            mContentNoChildren = mView!!.findViewById(R.id.content_no_children)

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
        }

        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mView?.also {
            activity?.also { activity ->

                mProfileViewModel = ViewModelProviders.of(activity).get(ProfileViewModel::class.java)

                mProfileViewModel.mChildren.observe(activity, Observer { theChildren ->
                    mChildrenList = theChildren
                    if (!::mChildrenAdapter.isInitialized) {
                        mProgressBar.visibility = View.GONE
                        mChildrenAdapter =
                            ChildrenAdapter(theChildren as MutableList<Child>, activity, this@ChildrenFragment)
                        mViewAdapter = mChildrenAdapter
                        mRecyclerView.adapter = mViewAdapter

                    } else {

                    }
                    if (theChildren.isEmpty()) {
                        mContentNoChildren.visibility = View.VISIBLE
                    } else {
                        mContentNoChildren.visibility = View.GONE
                    }
                })
            }
        }
    }

    override fun <T> onItemClick(view: View?, position: Int?, url: String?, varObject: T?) {
    }

    override fun onClick(v: View?) {
    }

}
