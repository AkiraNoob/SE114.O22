package com.example.collabtask

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvItem: TextView = itemView.findViewById(R.id.etv_item)
}
