package com.ziggy.kdo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ziggy.kdo.BuildConfig
import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.ListItemChildrenBinding
import com.ziggy.kdo.databinding.ListItemFriendsBinding
import com.ziggy.kdo.listener.CustomOnItemClickListener
import com.ziggy.kdo.listener.diffutil.ChildDiffCallback
import com.ziggy.kdo.listener.diffutil.GiftDiffCallback
import com.ziggy.kdo.model.Child
import com.ziggy.kdo.model.Gift
import com.ziggy.kdo.model.User

/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.04.19
 */
class ChildrenAdapter (
    var children: MutableList<Child>? = null,
    private val context: Context? = null,
    private val customOnItemClick: CustomOnItemClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ChildrenViewHolder constructor(listItemChildrenBinding: ListItemChildrenBinding) :
        RecyclerView.ViewHolder(listItemChildrenBinding.root) {

        val childrenRecyclerViewBinding: ListItemChildrenBinding = listItemChildrenBinding

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ListItemChildrenBinding>(
            layoutInflater,
            R.layout.list_item_children,
            parent,
            false
        )
        return ChildrenAdapter.ChildrenViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return children!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val children = children!![position]
        if (holder is ChildrenViewHolder) {
            holder.itemView.setOnClickListener {theView ->
                customOnItemClick?.onItemClick(varObject = children)
            }
            holder.childrenRecyclerViewBinding.childModel = children
        }
    }

    fun removeChildList(userId: String?) {
        userId?.let { theId ->
            loop@ for ((index, theChildRemove) in this.children!!.withIndex()) {
                if (theChildRemove.id == theId) {
                    this.children!!.removeAt(index)
                    notifyItemRangeRemoved(index, 1)
                    break@loop
                }
            }
        }
    }

    fun updateChildList(childrenList: List<Child>) {
        val diffCallback = ChildDiffCallback(this.children as List<Child>, childrenList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateChild(child: Child?){
        child?.also { theChild ->
            loop@ for ((index, theGiftUpdate) in this.children!!.withIndex()) {
                if (theGiftUpdate.id == theChild.id) {
                    this.children!![index] = theChild
                    notifyItemChanged(index)
                    break@loop
                }
            }
        }
    }

}