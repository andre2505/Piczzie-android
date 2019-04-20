package com.ziggy.kdo.utils

import android.content.Context
import android.view.ViewTreeObserver
import com.ziggy.kdo.R
import androidx.core.view.ViewCompat.setTranslationY
import android.widget.ImageView
import android.widget.ScrollView


/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.04.17
 */
class ScrollPositionObserver(private val imageView: ImageView, private val mScrollView:ScrollView, context:Context): ViewTreeObserver.OnScrollChangedListener {

    val mImageViewHeight = context.resources.getDimensionPixelSize(R.dimen.image_scroll)

    override fun onScrollChanged() {
        val scrollY = Math.min(Math.max(mScrollView.scrollY, 0), imageView.height)

        // changing position of ImageView

        imageView.translationY = (scrollY / 2).toFloat()

        // alpha you could set to ActionBar background
        val alpha = scrollY / mImageViewHeight
    }
}