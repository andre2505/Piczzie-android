package com.ziggy.kdo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ziggy.kdo.R

class EditProfileAdapter(var list: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class EditProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val editText: TextView = itemView.findViewById(R.id.list_item_edit_profil_text)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as EditProfileViewHolder
        holder.editText.text = list[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val holder = EditProfileViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_edit_profile,
                parent, false
            )
        )

        return holder

    }
}