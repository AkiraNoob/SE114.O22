package com.example.collabtask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.collabtask.model.Board

class DashboardGroupItemAdapter(private val teamId: String, private val itemList: List<Board>, private val navController: NavController) :
    RecyclerView.Adapter<DashboardGroupItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DashboardGroupItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_group_board_item_fragment, parent, false)
        return DashboardGroupItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardGroupItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.textBoardName.text = currentItem.name
        holder.wrapper.setOnClickListener {
        }
        if (position == itemCount - 1) {
            holder.divider.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

class DashboardGroupItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textBoardName: TextView = itemView.findViewById(R.id.dashboard_group_board_item_name)
    val wrapper: LinearLayout = itemView.findViewById(R.id.dashboard_group_board_item_wrapper)
    val divider: Button = itemView.findViewById<Button>(R.id.dashboard_group_board_item_divider)
}