package com.example.collabtask

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GroupDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GroupTableAdapter
    private lateinit var menuButton: ImageView
    private lateinit var memberDialog: AlertDialog

    private val tableData = mutableListOf("Bảng 1")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group_detail)

        recyclerView = findViewById(R.id.view_table)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = GroupTableAdapter(tableData)
        recyclerView.adapter = adapter

        // Menu button
        menuButton = findViewById(R.id.imageView2)
        menuButton.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.inflate(R.menu.menu_group_detail)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.create_table -> {
                        showAddTableDialog()
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

    private fun showAddTableDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.dialog_add_table, null)

        val editTableName = view.findViewById<EditText>(R.id.edit_table_name)
        val closeDialog = view.findViewById<ImageView>(R.id.close_dialog)


        val dialog = builder.setView(view).create()


        closeDialog.setOnClickListener {
            dialog.dismiss()
        }

        editTableName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val tableName = editTableName.text.toString()
                if (tableName.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập tên bảng", Toast.LENGTH_SHORT).show()
                } else {

                    tableData.add("Bảng $tableName")

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