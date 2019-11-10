package com.ziggy.kdo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ziggy.kdo.BuildConfig
import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.ListItemSearchUserBinding
import com.ziggy.kdo.listener.CustomOnItemClickListener
import com.ziggy.kdo.model.Gift
import com.ziggy.kdo.model.User

/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.03.12
 */
class SearchFriendsAdapter(
    private var users: MutableList<User>? = null,
    private val context: Context? = null,
    private val customOnItemClick: CustomOnItemClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var SearchHolder = 1

    var ProgressHolder = 2

    class SearchFriendsViewHolder constructor(listItemSearchUserBinding: ListItemSearchUserBinding) :
        RecyclerView.ViewHolder(listItemSearchUserBinding.root) {

        val searchRecyclerViewBinding = listItemSearchUserBinding
    }

    class ProgressBarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var holder: RecyclerView.ViewHolder?

        if (viewType == SearchHolder) {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ListItemSearchUserBinding>(
                layoutInflater,
                R.layout.list_item_search_user,
                parent,
                false
            )
            return SearchFriendsAdapter.SearchFriendsViewHolder(binding)
        } else {
            holder = SearchFriendsAdapter.ProgressBarViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.view_loading_search,
                    parent, false
                )
            )
            return holder
        }
    }

    override fun getItemCount(): Int {
        return users!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (users!![position].id == null) {
            ProgressHolder
        } else {
            SearchHolder
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = users!![position]
        if (holder is SearchFriendsViewHolder) {
            user.photo?.let { theImageUser ->
                Glide
                    .with(holder.itemView)
                    .load(BuildConfig.ENDPOINT + theImageUser)
                    .apply(
                        RequestOptions
                            .circleCropTransform()
                            .placeholder(R.drawable.ic_user_profile)
                            .error(R.drawable.ic_user_profile)
                    )
                    .into(holder.searchRecyclerViewBinding.listItemSearchImageProfil)
            }
            holder.searchRecyclerViewBinding.userModel = user

            holder.itemView.setOnClickListener {
                customOnItemClick?.onItemClick(view = it, varObject = user)
            }
        }
    }

    fun updateUsers(users: MutableList<User>?) {
        users?.let { theListUser ->
            this.users?.clear()
            this.users?.addAll(users)
            notifyDataSetChanged()
        }
    }

    fun addLoading() {
        users?.add(User())
        notifyItemInserted(users!!.size - 1)
    }

    fun addLoadingBeginning() {
        users!!.clear()
        users?.add(User())
        notifyDataSetChanged()
    }

    fun getLastItem(): User? {
        if (users!!.isNotEmpty()) {
            return users!!.last()
        }
        return null
    }
}