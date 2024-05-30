package com.example.collabtask

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.collabtask.model.User
import com.squareup.picasso.Picasso

class CardUserJoinedItemAdapter(private val itemList: List<User>) :
    RecyclerView.Adapter<CardUserJoinedItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardUserJoinedItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.board_card_user_join_fragment, parent, false)
        return CardUserJoinedItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardUserJoinedItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        if(currentItem.avatar != null) {
            Picasso.get().load(currentItem.avatar).into(holder.cardUserJoined)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

class CardUserJoinedItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cardUserJoined: ImageView = itemView.findViewById(R.id.card_user_joined_item)
}