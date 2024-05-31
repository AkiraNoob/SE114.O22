package com.example.collabtask

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collabtask.model.Board

class BoardDetail : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BoardAdapter
    private lateinit var menuButton: ImageView
    private lateinit var memberDialog: AlertDialog

    private val tableData = mutableListOf("Danh sách 1")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.board_detail)

        recyclerView = findViewById(R.id.view_table)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = BoardAdapter(tableData)
        recyclerView.adapter = adapter

        // Menu button
        menuButton = findViewById(R.id.imageView2)
        menuButton.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.inflate(R.menu.menu_board_detail)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.create_list -> {
                        showAddListDialog()
                        true
                    }

                    R.id.member -> {
                        showMemberDialog()
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
        }

    }

    private fun showMemberDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.dialog_add_member, null)

        val editEmail = view.findViewById<EditText>(R.id.edit_email)
        val closeDialog = view.findViewById<ImageView>(R.id.close_dialog)
        val memberList = view.findViewById<RecyclerView>(R.id.member_list)

        val memberAdapter = MemberAdapter(mutableListOf())

        memberList.layoutManager = LinearLayoutManager(this)
        memberList.adapter = memberAdapter

        closeDialog.setOnClickListener {
            memberDialog.dismiss()
        }

        editEmail.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val email = editEmail.text.toString()
                if (email.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show()
                } else {

                    memberAdapter.addMember(email)
                    editEmail.setText("")
                }
                true
            } else {
                false
            }
        }

        memberDialog = builder.setView(view).create()
        memberDialog.show()
    }

    private fun showAddListDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.dialog_add_list, null)

        val editListName = view.findViewById<EditText>(R.id.edit_list_name)
        val editGroupName = view.findViewById<EditText>(R.id.edit_group_name)
        val closeDialog = view.findViewById<ImageView>(R.id.close_dialog)

        val dialog = builder.setView(view).create()

        closeDialog.setOnClickListener {
            dialog.dismiss()
        }

        editListName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val listName = editListName.text.toString()
                val groupName = editGroupName.text.toString()
                if (listName.isEmpty() && groupName.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Vui lòng nhập tên danh sách và tên nhóm",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    tableData.add("Danh sách $listName")

                    adapter.notifyItemInserted(tableData.size - 1)

                    dialog.dismiss()
                }
                true
            } else {
                false
            }
        }

        dialog.show()
    }
}

class BoardAdapter(private val data: MutableList<String>) :
    RecyclerView.Adapter<BoardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textRow: TextView = itemView.findViewById(R.id.text_row)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.board_fragment, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.textRow.text = item
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addList(listName: String) {
        data.add(listName)
        notifyItemInserted(data.size - 1)
    }
}