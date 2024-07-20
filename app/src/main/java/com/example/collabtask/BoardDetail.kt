package com.example.collabtask

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
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
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collabtask.databinding.BoardDetailBinding
import com.example.collabtask.databinding.DashboardFragmentBinding
import com.example.collabtask.model.Board
import com.example.collabtask.model.BoardList
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class BoardDetail : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: BoardDetailBinding

    private lateinit var adapter: BoardAdapter
    private lateinit var memberDialog: AlertDialog
    val tableData = mutableListOf<BoardList>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = BoardDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showMemberDialog()
    }

    override fun onStart() {
        super.onStart()

        binding.boardViewTable.layoutManager = LinearLayoutManager(applicationContext)

        binding.boardImageView2.setOnClickListener {
            val popupMenu = PopupMenu(applicationContext, it).apply {
                setOnMenuItemClickListener(this@BoardDetail)
            }
            popupMenu.menuInflater.inflate(R.menu.menu_board_detail, popupMenu.menu)
            popupMenu.setForceShowIcon(true)
            popupMenu.show()
        }

        lifecycleScope.launch {
            val boardListFirestore = FirebaseFirestore.getInstance().collection("boardList")
                .whereEqualTo("boardId", intent.getStringExtra("boardId")).get().await()

            for (documents in boardListFirestore.documents) {
                val boardList = documents.toObject(BoardList::class.java)
                if (boardList != null) {
                    boardList.id = documents.id
                    tableData.add(boardList)
                }
            }

            adapter = BoardAdapter(tableData, { navigateToBoardListDetail(it) })
            binding.boardViewTable.adapter = adapter
        }

        // Menu button

    }
    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.create_list -> {
                showAddListDialog()
                true
            }

            R.id.member -> {
                memberDialog.show()
                true
            }

            else -> false
        }
    }

    private fun showMemberDialog() {
        val builder = AlertDialog.Builder(applicationContext)
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.dialog_add_member, null)

        val editEmail = view.findViewById<EditText>(R.id.edit_email)
        val closeDialog = view.findViewById<ImageView>(R.id.close_dialog)
        val memberList = view.findViewById<RecyclerView>(R.id.member_list)

        val memberAdapter = MemberAdapter(mutableListOf())

        memberList.layoutManager = LinearLayoutManager(applicationContext)
        memberList.adapter = memberAdapter

        closeDialog.setOnClickListener {
            memberDialog.dismiss()
        }

        editEmail.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val email = editEmail.text.toString()
                if (email.isEmpty()) {
                    Toast.makeText(applicationContext, "Vui lòng nhập email", Toast.LENGTH_SHORT)
                        .show()
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
    }

    private fun showAddListDialog() {
        val builder = AlertDialog.Builder(applicationContext)
        val inflater = LayoutInflater.from(applicationContext)
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
                        applicationContext,
                        "Vui lòng nhập tên danh sách và tên nhóm",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    tableData.add(
                        BoardList(
                            title = "Danh sách $listName",
                            boardId = "78pvKSFbeHOMVbKPWdqo"
                        )
                    )

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

    private fun navigateToBoardListDetail(boardListId: String) {
        val intent = Intent(this, BoardListDetailFragment::class.java)
        intent.putExtra("boardListId", boardListId)
        tableData.clear()
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        tableData.clear()
    }

}

class BoardAdapter(
    private val data: MutableList<BoardList>,
    private val navigateToBoardListDetail: (String) -> Unit
) :
    RecyclerView.Adapter<BoardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView5: TextView = itemView.findViewById(R.id.textView5)
        val card: CardView = itemView.findViewById(R.id.board_fragment_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.board_fragment, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.textView5.text = item.title
        holder.card.setCardBackgroundColor(Color.parseColor(item.color))
        holder.card.setOnClickListener {
            navigateToBoardListDetail(item.id)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}