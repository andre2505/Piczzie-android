package com.ziggy.kdo.utils

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.TextView
import com.ziggy.kdo.R

object CustomDialog {

    fun getDialogLoading(id: Int, activity: Context?): Dialog {

       val dialog = Dialog(activity!!)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.view_dialog_reserved)

        val textDelete: TextView = dialog.findViewById(id)

        textDelete.text = activity.getString(id)

        return dialog
    }
}