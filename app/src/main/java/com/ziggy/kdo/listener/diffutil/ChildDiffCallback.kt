package com.ziggy.kdo.listener.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.ziggy.kdo.model.Child
import com.ziggy.kdo.model.Gift

class ChildDiffCallback (private val oldChildren: List<Child>, private val newChildren: List<Child>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldChildren[oldItemPosition].id == newChildren[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldChildren.size
    }

    override fun getNewListSize(): Int {
        return newChildren.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return true
    }

}