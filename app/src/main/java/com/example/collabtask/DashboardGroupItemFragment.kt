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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class DashboardGroupItemAdapter(
    private val teamId: String,
    private val itemList: QuerySnapshot?,
    private val navigateToBoardDetail: (String) -> Unit
) :
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
        if (itemList != null) {
            val currentItem = itemList.documents[position]
            holder.textBoardName.text = currentItem.get("name").toString()
            holder.wrapper.setOnClickListener {
                navigateToBoardDetail(currentItem.id)
            }
            if (position == itemCount - 1) {
                holder.divider.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        if (itemList != null)
            return itemList.size()

        return 0
    }
}

class DashboardGroupItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textBoardName: TextView = itemView.findViewById(R.id.dashboard_group_board_item_name)
    val wrapper: LinearLayout = itemView.findViewById(R.id.dashboard_group_board_item_wrapper)
    val divider: Button = itemView.findViewById<Button>(R.id.dashboard_group_board_item_divider)
}