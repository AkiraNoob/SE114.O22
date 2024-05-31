package com.example.collabtask

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.collabtask.model.Label

class CardLabelItemAdapter(private val itemList: List<Label>) :
    RecyclerView.Adapter<CardLabelItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardLabelItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_label_item_fragment, parent, false)
        return CardLabelItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardLabelItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.cardLabelItem.setBackgroundColor(Color.parseColor(currentItem.color))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

class CardLabelItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cardLabelItem: ConstraintLayout = itemView.findViewById(R.id.card_label_item)
}