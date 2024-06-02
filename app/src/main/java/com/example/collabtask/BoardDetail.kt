package com.example.collabtask

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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

class BoardDetail : Fragment() {

    private var _binding: BoardDetailBinding? = null
    private val binding get() = _binding!!


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BoardAdapter
    private lateinit var menuButton: ImageView
    private lateinit var memberDialog: AlertDialog
    val tableData = mutableListOf<BoardList>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BoardDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.board_view_table)
        recyclerView.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch {
            val boardListFirestore = FirebaseFirestore.getInstance().collection("boardList")
                .whereEqualTo("boardId", "78pvKSFbeHOMVbKPWdqo").get().await()

            for (documents in boardListFirestore.documents) {
                val boardList = documents.toObject(BoardList::class.java)
                if (boardList != null) {
                    tableData.add(boardList)
                }
            }

            adapter = BoardAdapter(tableData, findNavController())
            recyclerView.adapter = adapter
        }

        // Menu button
        menuButton = view.findViewById(R.id.board_imageView2)
        menuButton.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
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
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_add_member, null)

        val editEmail = view.findViewById<EditText>(R.id.edit_email)
        val closeDialog = view.findViewById<ImageView>(R.id.close_dialog)
        val memberList = view.findViewById<RecyclerView>(R.id.member_list)

        val memberAdapter = MemberAdapter(mutableListOf())

        memberList.layoutManager = LinearLayoutManager(context)
        memberList.adapter = memberAdapter

        closeDialog.setOnClickListener {
            memberDialog.dismiss()
        }

        editEmail.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val email = editEmail.text.toString()
                if (email.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập email", Toast.LENGTH_SHORT).show()
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
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
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
                        context,
                        "Vui lòng nhập tên danh sách và tên nhóm",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    tableData.add(BoardList(title = "Danh sách $listName", boardId = "78pvKSFbeHOMVbKPWdqo"))

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

class BoardAdapter(private val data: MutableList<BoardList>, private val navController: NavController) :
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
            navController.navigate(R.id.boardList_detail_fragment)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}