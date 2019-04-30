package com.ziggy.kdo.ui.fragment.children


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
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
import com.ziggy.kdo.model.Child
import com.ziggy.kdo.model.User
import com.ziggy.kdo.ui.adapter.ChildrenAdapter
import com.ziggy.kdo.ui.base.BaseFragment
import com.ziggy.kdo.ui.fragment.profile.ProfileViewModel
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService


/**
 * A simple [Fragment] subclass.
 *
 */
class ChildrenFragment : BaseFragment(), CustomOnItemClickListener, View.OnClickListener {

    private lateinit var mProfileViewModel: ProfileViewModel

    private lateinit var mChildViewModel: ChildViewModel

    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mViewAdapter: RecyclerView.Adapter<*>

    private lateinit var mViewManager: RecyclerView.LayoutManager

    private lateinit var mLinearLayoutManager: LinearLayoutManager

    private lateinit var mChildrenAdapter: ChildrenAdapter

    private lateinit var mProgressBar: ProgressBar

    private lateinit var mContentNoChildren: LinearLayout

    private lateinit var mContentAddChildrenFragment: LinearLayout

    private var mView: View? = null

    private var mDialog: Dialog? = null

    private var mUser: User? = null

    private var mChildrenList: MutableList<Child>? = null

    private var mChild: Child? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.also { activity ->

            mProfileViewModel = ViewModelProviders.of(activity).get(ProfileViewModel::class.java)

            mChildViewModel = ViewModelProviders.of(activity, mViewModeFactory).get(ChildViewModel::class.java)

            mChildViewModel.mChildrenList.observe(this@ChildrenFragment, Observer { theChildren ->

                if (!::mChildrenAdapter.isInitialized) {
                    mProgressBar.visibility = View.GONE
                    mChildrenAdapter =
                        ChildrenAdapter(theChildren as MutableList<Child>, activity, this@ChildrenFragment)
                    mViewAdapter = mChildrenAdapter
                    mRecyclerView.adapter = mViewAdapter

                } else {
                    mProfileViewModel.mChildren.value = theChildren
                    mChildrenAdapter.updateChildList(theChildren)
                }
                if (theChildren.isEmpty()) {
                    mContentNoChildren.visibility = View.VISIBLE
                } else {
                    mContentNoChildren.visibility = View.GONE
                }
            })

            mChildViewModel.mUpdateSuccess.observe(activity, Observer { theSuccess ->
                when (theSuccess) {
                    Error.NO_ERROR -> {
                        mChildrenAdapter.updateChild(mChildViewModel.mChild.value)
                    }
                    else -> {
                    }
                }
            })


            mChildViewModel.mDeleteSuccess.observe(activity, Observer { theSuccess ->
                when (theSuccess) {
                    Error.NO_ERROR -> {
                        mChildrenAdapter.removeChildList(mChildViewModel.mChild.value?.id)
                        mProfileViewModel.mChildren.value = mChildrenAdapter.children
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
            mView = inflater.inflate(R.layout.fragment_children, container, false)

            mRecyclerView = mView!!.findViewById(R.id.children_recycler)
            mProgressBar = mView!!.findViewById(R.id.children_progressbar)
            mContentNoChildren = mView!!.findViewById(R.id.content_no_children)
            mContentAddChildrenFragment = mView!!.findViewById(R.id.children_add_child)

            mContentAddChildrenFragment.setOnClickListener(this@ChildrenFragment)

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
            this.mChildrenList = mProfileViewModel.mChildren.value
            mChildViewModel.mChildrenList.value = mChildrenList
        }

        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(mView?.windowToken, 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.also { theActivity ->
            mChildViewModel.mDeleteSuccess.removeObservers(theActivity)
            mChildViewModel.mUpdateSuccess.removeObservers(theActivity)
            mChildViewModel.mChildrenList.removeObservers(this@ChildrenFragment)
        }
    }

    override fun <T> onItemClick(view: View?, position: Int?, url: String?, varObject: T?) {
        mChild = varObject as Child
        mChildViewModel.mChild.value = mChild?.copy()
        mChild?.also {
            Navigation.findNavController(mView!!).navigate(R.id.action_addChildFragment_to_childProfileFragment)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.children_add_child -> {
                Navigation.findNavController(mView!!).navigate(R.id.action_childrenFragment_to_addChildFragment)
            }
        }
    }
}
