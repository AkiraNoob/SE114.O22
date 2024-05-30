package com.example.collabtask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroupTableAdapter(private val data: MutableList<String>) : RecyclerView.Adapter<GroupTableAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textRow: TextView = itemView.findViewById(R.id.text_row)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == 0) {

            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.table_row_even, parent, false)
            return ViewHolder(itemView)
        } else {

            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.table_row_odd, parent, false)
            return ViewHolder(itemView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position % 2 == 0) {

            return 0
        } else {

            return 1
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.textRow.text = item
    }

    override fun getItemCount(): Int {
        return data.size
    }


    fun addTable(tableName: String) {
        data.add(tableName)
        notifyItemInserted(data.size - 1)
    }
}