package com.ziggy.kdo.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ziggy.kdo.R
import com.ziggy.kdo.listener.CustomOnItemClickListener

/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.03.12
 */
class GridImageAdapter(
    private var mImageUrls: MutableList<String>? = null,
    private var mSizeImge: Int? = null,
    private val context: Context? = null,
    private val customOnItemClick: CustomOnItemClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.gridImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val holder = GridImageAdapter.ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_grid_imageview,
                parent, false
            )
        )

        return holder
    }

    override fun getItemCount(): Int {
        return mImageUrls!!.size
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GridImageAdapter.ImageViewHolder) {
            mSizeImge?.let { theImgSize ->
                Glide
                    .with(holder.itemView)
                    .load(mImageUrls!![position])
                    .apply(RequestOptions().override(theImgSize, theImgSize))
                    .into(holder.image)
            }

            holder.image.setOnClickListener {
                mImageUrls?.let { theImage ->
                    customOnItemClick?.onItemClick<Nothing>(url = theImage[position])
                }
            }
        }
    }

    fun addImage(list: List<String>) {
        mImageUrls!!.addAll(mImageUrls!!.size -1, list)
        notifyItemRangeInserted(mImageUrls!!.size -1, list.size )
    }
}