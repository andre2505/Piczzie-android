package com.ziggy.kdo.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ziggy.kdo.BuildConfig
import com.ziggy.kdo.R
import com.ziggy.kdo.listener.CustomOnItemClickListener
import com.ziggy.kdo.model.Gift
import android.text.method.TextKeyListener.clear
import android.util.Log
import android.widget.ProgressBar
import androidx.recyclerview.widget.DiffUtil
import com.ziggy.kdo.listener.diffutil.GiftDiffCallback


/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.03.27
 */
class GridImageMyGift(
    var gifts: MutableList<Gift>? = null,
    private var mSizeImge: Int? = null,
    private val context: Context? = null,
    private val customOnItemClick: CustomOnItemClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val GiftHolder = 1

        val ProgressHolder = 2
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.gridImageView)
    }

    class ProgressBarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressBar: ProgressBar = itemView.findViewById(R.id.loading_endless_scroll)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var holder: RecyclerView.ViewHolder? = null

        if (viewType == GiftHolder) {
            holder = GridImageMyGift.ImageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_grid_imageview,
                    parent, false
                )
            )
            return holder
        } else if (viewType == ProgressHolder) {
            holder = GridImageMyGift.ProgressBarViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.view_loading_recycler_view,
                    parent, false
                )
            )
            return holder
        }
        return holder!!
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GridImageMyGift.ImageViewHolder) {
            gifts?.let { theGifts ->
                mSizeImge?.let { theImgSize ->

                    var thumbnail = Glide.with(holder.itemView).load(R.drawable.ic_broken_image_png)
                        .apply(RequestOptions.overrideOf(theImgSize, theImgSize))

                    Glide
                        .with(holder.image)
                        .load(BuildConfig.ENDPOINT + theGifts[position].image)
                        .apply(
                            RequestOptions()
                                .override(theImgSize, theImgSize)
                                .centerCrop()
                        )
                        .thumbnail(thumbnail)
                        .thumbnail(0.5F)
                        .into(holder.image)
                }

                holder.image.setOnClickListener {
                    theGifts[position].image?.let { theImage ->
                        customOnItemClick?.onItemClick<Any>(varObject = gifts!![position])
                    }

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return gifts!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (gifts!![position].id == null) {
            ProgressHolder
        } else {
            GiftHolder
        }
    }

    fun updateList(gifts: List<Gift>?) {
        gifts?.let {
            this.gifts!!.addAll(gifts)
            notifyItemRangeInserted(this.gifts!!.size - 1, gifts.size)
        }
    }

    fun addGiftList(gift: Gift?) {
        gift?.let { theGift ->
            this.gifts!!.add(0, theGift)
            notifyItemInserted(0)
        }
    }

    fun removeGiftList(gift: Gift?) {
        gift?.let { theGift ->
            loop@ for ((index, theGiftRemove) in this.gifts!!.withIndex()) {
                if (theGiftRemove.id == theGift.id) {
                    this.gifts!!.removeAt(index)
                    notifyItemRangeRemoved(index, 1)
                    break@loop
                }
            }
        }
    }

    fun addLoading() {
        val oldGift = this.gifts
        gifts?.add(Gift())
        val diffCallback = GiftDiffCallback(oldGift as MutableList<Gift>, this.gifts as MutableList<Gift>)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
    }

    fun removeLoading() {
        if(this.gifts!!.isNotEmpty() ) {
            val oldGift = this.gifts
            gifts?.removeAt(gifts!!.size - 1)
            val diffCallback = GiftDiffCallback(oldGift as MutableList<Gift>, this.gifts as MutableList<Gift>)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            diffResult.dispatchUpdatesTo(this)
        }
    }

    fun addListOnRestore() {
        gifts?.addAll(getList())
        notifyDataSetChanged()
    }

    fun getList(): List<Gift> {
        return this.gifts!!
    }

    fun updateGift(gift: Gift?){
        gift?.let { theGift ->
            loop@ for ((index, theGiftUpdate) in this.gifts!!.withIndex()) {
                if (theGiftUpdate.id == theGift.id) {
                    this.gifts!![index] = theGift
                    notifyItemChanged(index)
                    break@loop
                }
            }
        }
    }
}