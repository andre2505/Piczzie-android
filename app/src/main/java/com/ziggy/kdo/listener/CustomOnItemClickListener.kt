package com.ziggy.kdo.listener

import android.view.View

/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.03.17
 */
interface CustomOnItemClickListener {
    fun <T> onItemClick(view: View? = null, position: Int? = null, url: String? = null, varObject: T? = null)
}