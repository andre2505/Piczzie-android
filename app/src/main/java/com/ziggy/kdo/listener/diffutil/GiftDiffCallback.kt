package com.ziggy.kdo.listener.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.ziggy.kdo.model.Gift

/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.03.28
 */
class GiftDiffCallback(private val oldGifts: List<Gift>, private val newGifts: List<Gift>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldGifts[oldItemPosition].id == newGifts[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldGifts.size
    }

    override fun getNewListSize(): Int {
        return newGifts.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return true
    }

}