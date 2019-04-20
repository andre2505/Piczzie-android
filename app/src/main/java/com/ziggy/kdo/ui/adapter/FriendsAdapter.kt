package com.ziggy.kdo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ziggy.kdo.BuildConfig
import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.ListItemFriendsBinding
import com.ziggy.kdo.databinding.ListItemSearchUserBinding
import com.ziggy.kdo.listener.CustomOnItemClickListener
import com.ziggy.kdo.model.Gift
import com.ziggy.kdo.model.User
import com.ziggy.kdo.ui.fragment.profile.ProfileViewModel

/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.04.17
 */
class FriendsAdapter(
    private var users: MutableList<User>? = null,
    private val context: Context? = null,
    private val customOnItemClick: CustomOnItemClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class FriendsViewHolder constructor(listItemFriendsBinding: ListItemFriendsBinding) :
        RecyclerView.ViewHolder(listItemFriendsBinding.root) {

        val searchRecyclerViewBinding: ListItemFriendsBinding = listItemFriendsBinding

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ListItemFriendsBinding>(
            layoutInflater,
            R.layout.list_item_friends,
            parent,
            false
        )
        return FriendsAdapter.FriendsViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return users!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = users!![position]
        if (holder is FriendsViewHolder) {
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
                    .into(holder.searchRecyclerViewBinding.listItemFriendsImageProfil)
            }
            holder.searchRecyclerViewBinding.listItemFriendsSuppress.setOnClickListener { theButton ->
                customOnItemClick!!.onItemClick(view = theButton, varObject = user)
            }
            holder.searchRecyclerViewBinding.userModel = user
        }
    }

    fun removeFriendList(userId: String?) {
        userId?.let { theId ->
            loop@ for ((index, theUserRemove) in this.users!!.withIndex()) {
                if (theUserRemove.id == theId) {
                    this.users!!.removeAt(index)
                    notifyItemRangeRemoved(index, 1)
                    break@loop
                }
            }
        }
    }

    fun updateList(friendsList: List<User>) {
        this.users?.clear()
        notifyDataSetChanged()

        this.users?.addAll(friendsList)
        notifyDataSetChanged()
    }

}