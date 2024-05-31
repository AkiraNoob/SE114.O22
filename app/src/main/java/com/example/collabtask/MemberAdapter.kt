package com.example.collabtask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MemberAdapter(private val members: MutableList<Member>) : RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

    class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val memberName: TextView = itemView.findViewById(R.id.member_name)
        val memberEmail: TextView = itemView.findViewById(R.id.member_email)
        val memberDelete: ImageView = itemView.findViewById(R.id.member_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.member_item, parent, false)
        return MemberViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = members[position]
        holder.memberName.text = member.name
        holder.memberEmail.text = member.email
        holder.memberDelete.setOnClickListener {
            removeMember(position)
        }
    }

    override fun getItemCount(): Int {
        return members.size
    }


    fun addMember(email: String) {
        val name = email.substringBefore("@")
        val newMember = Member(name, email)
        members.add(newMember)
        notifyItemInserted(members.size - 1)
    }


    fun removeMember(position: Int) {
        members.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, members.size)
    }
}


data class Member(val name: String, val email: String)