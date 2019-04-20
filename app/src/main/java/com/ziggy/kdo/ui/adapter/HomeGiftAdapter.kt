package com.ziggy.kdo.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ziggy.kdo.BR
import com.ziggy.kdo.BuildConfig
import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.ListItemGiftFriendsBinding
import com.ziggy.kdo.model.Gift
import com.ziggy.kdo.model.User
import com.ziggy.kdo.ui.fragment.home.HomeViewModel
import java.util.*


class HomeGiftAdapter(
    private var gifts: MutableList<Gift>?,
    private val context: Context,
    private val homeViewModel: HomeViewModel
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    var GiftHolder = 1

    var ProgressHolder = 2

    lateinit var alertDialog: AlertDialog

    var position: Int = 0

    class HomeGiftViewHolder constructor(listItemGiftFriendsBinding: ListItemGiftFriendsBinding) :
        RecyclerView.ViewHolder(listItemGiftFriendsBinding.root) {
        val homeRecyclerViewBinding = listItemGiftFriendsBinding
    }

    class ProgressBarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressBar: ProgressBar = itemView.findViewById(R.id.loading_endless_scroll)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var holder: RecyclerView.ViewHolder? = null

        if (viewType == GiftHolder) {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ListItemGiftFriendsBinding>(
                layoutInflater,
                R.layout.list_item_gift_friends,
                parent,
                false
            )

            holder = HomeGiftViewHolder(binding)
            return holder
        } else if (viewType == ProgressHolder) {
            holder = ProgressBarViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.view_loading_recycler_view,
                    parent, false
                )
            )
            return holder
        }
        return holder!!
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)

        val gift = gifts!![position]

        if (holder is HomeGiftViewHolder) {

            if (payloads.isNotEmpty()) {
                payloads[0].let { theUser ->
                    holder.homeRecyclerViewBinding.giftModel?.userReserved = theUser as? User
                    holder.homeRecyclerViewBinding.notifyPropertyChanged(BR.userReserved)
                } ?: kotlin.run {
                    holder.homeRecyclerViewBinding.giftModel?.userReserved = null
                    holder.homeRecyclerViewBinding.notifyPropertyChanged(BR.userReserved)
                }
            } else {
                gift.image?.let {
                    Glide
                        .with(holder.itemView)
                        .load(BuildConfig.ENDPOINT + gift.image)
                        .apply(
                            RequestOptions
                                .noTransformation()
                                .error(R.drawable.drawable_broken_image)

                        )
                        .into(holder.homeRecyclerViewBinding.listItemImageGift)
                }

                gift.user?.photo?.let { imageUser ->
                    Glide
                        .with(holder.itemView)
                        .load(BuildConfig.ENDPOINT + imageUser)
                        .apply(
                            RequestOptions
                                .circleCropTransform()
                                .placeholder(R.drawable.ic_user_profile)
                                .error(R.drawable.ic_user_profile)
                        )
                        .into(holder.homeRecyclerViewBinding.listItemImageProfil)
                }

                holder.homeRecyclerViewBinding.listItemImageReserved.setOnClickListener {
                    this.position = position
                    homeViewModel.updateReservedGiftUser(gift)
                }

                holder.homeRecyclerViewBinding.giftModel = gift
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

    override fun onClick(v: View?) {

    }

    fun addItems(olderGifts: MutableList<Gift>?) {
        olderGifts?.let {
            gifts?.addAll(olderGifts)
            notifyItemRangeInserted(gifts!!.size - 1, olderGifts.size)
        }
    }

    fun addItemsOnRefresh(youngerGifts: MutableList<Gift>?) {
        youngerGifts?.let {
            gifts!!.addAll(0, youngerGifts)
            notifyItemRangeInserted(0, youngerGifts.size)
        }
    }

    fun addLoading() {
        gifts?.add(Gift())
        notifyItemInserted(gifts!!.size - 1)
    }

    fun removeLoading() {
        gifts?.removeAt(gifts!!.size - 1)
        notifyItemRemoved(gifts!!.size - 1)
    }

    fun updateItem(gift: Gift?) {
        gift?.let { theGift ->
            theGift.userReserved?.let {
                notifyItemChanged(this.position, theGift.userReserved)
            } ?: kotlin.run {
                notifyItemChanged(this.position, 0)
            }
        }
    }

    fun getFirstElement(): Date? = gifts!![0].date

}
