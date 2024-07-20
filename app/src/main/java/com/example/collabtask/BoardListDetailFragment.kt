package com.example.collabtask

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collabtask.databinding.BoardListDetailFragmentBinding
import com.example.collabtask.model.Card
import com.example.collabtask.model.User
import com.example.collabtask.use_case.BoardListDetailApiUseCases
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class BoardListDetailFragment : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    private lateinit var binding: BoardListDetailFragmentBinding
    private var itemList: MutableList<Card> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BoardListDetailFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.boardCardsList.layoutManager = LinearLayoutManager(applicationContext)

        binding.menuBtn.setOnClickListener {
            val popup = PopupMenu(applicationContext, it).apply {
                setOnMenuItemClickListener(this@BoardListDetailFragment)
            }
            popup.menuInflater.inflate(R.menu.add_card_menu, popup.menu)
            popup.setForceShowIcon(true)
            popup.show()
        }

        lifecycleScope.launch {
            itemList = BoardListDetailApiUseCases.getCardsOfBoardList(
                intent.getStringExtra("boardListId").toString()
            ).toMutableList()

            binding.boardCardsList.adapter =
                BoardDetailAdapter(itemList, { navigateToCardDetail(it) })
        }
    }

    private fun addCardSuccessCallback(item: DocumentReference) {
        Log.i("hehe", "got it")
        lifecycleScope.launch {
            val card = item.get().await().toObject(Card::class.java)
            if (card != null) {
                Log.i("hehe", card.toString())
                itemList.add(card)
                binding.boardCardsList.adapter!!.notifyItemChanged(itemList.size - 1)
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_card -> {
                val dialog =
                    AddCardDialogFragment(
                        intent.getStringExtra("boardListId").toString(),
                        { addCardSuccessCallback(it) })
                dialog.show(supportFragmentManager, "add_card_dialog")
                true
            }

            else -> false
        }
    }

    private fun navigateToCardDetail(cardId: String) {
        val intent = Intent(this, CardInformationActivity::class.java)
        intent.putExtra("cardId", cardId)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        itemList.clear()
    }
}

class BoardDetailAdapter(
    private val itemList: List<Card>,
    private val navigateToCardDetail: (String) -> Unit
) :
    RecyclerView.Adapter<BoardDetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardDetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.board_card_item_fragment, parent, false)
        return BoardDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardDetailViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.boardCardTitle.text = currentItem.name
        holder.boardCardDescription.text = currentItem.description

        if (currentItem.commentList.isNullOrEmpty()) {
            holder.boardCardCommentWrapper.visibility = View.GONE
        } else {
            holder.boardCardCommentNumOfContent.text = currentItem.commentList!!.size.toString()
        }

        if (currentItem.labelList.isNullOrEmpty()) {
            holder.boardCardLabelList.visibility = View.GONE
        }

        if (currentItem.userJoinedCard.isNullOrEmpty()) {
            holder.boardCardUserJoinedList.visibility = View.GONE
        }

        holder.bind(itemList[position])

        holder.boardCardItem.setOnClickListener {
            navigateToCardDetail(currentItem.id)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

class BoardDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val boardCardTitle: TextView = itemView.findViewById(R.id.board_card_title)
    val boardCardDescription: TextView = itemView.findViewById(R.id.board_card_description)
    val boardCardCommentWrapper: LinearLayout =
        itemView.findViewById(R.id.board_card_num_of_comment_wrapper)
    val boardCardCommentNumOfContent: TextView =
        itemView.findViewById(R.id.board_card_num_of_comment_text)
    val boardCardLabelList: RecyclerView = itemView.findViewById(R.id.board_card_labels_list)
    val boardCardUserJoinedList: RecyclerView = itemView.findViewById(R.id.board_card_users_list)

    val boardCardItem: LinearLayout = itemView.findViewById(R.id.board_card_item)

    fun bind(item: Card) {
        // Set up inner RecyclerView
//        if (!item.labelList.isNullOrEmpty()) {
//            val labelsListRecyclerView: RecyclerView =
//                itemView.findViewById(R.id.board_card_labels_list)
//
//            val layoutManager = LinearLayoutManager(itemView.context)
//            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
//
//            labelsListRecyclerView.layoutManager = layoutManager
//
//            labelsListRecyclerView.adapter = CardLabelItemAdapter(item.labelList!!)
//        }

        if (!item.userJoinedCard.isNullOrEmpty()) {
            val userJoinedList: MutableList<User> = mutableListOf()

            val usersListRecyclerView: RecyclerView =
                itemView.findViewById(R.id.board_card_users_list)
            usersListRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            usersListRecyclerView.adapter = CardUserJoinedItemAdapter(userJoinedList)

            CoroutineScope(Dispatchers.IO).launch {
                val userQuerySnapshot = FirebaseFirestore.getInstance().collection("user").whereIn(
                    FieldPath.documentId(),
                    item.userJoinedCard as List<String>
                ).get().await()

                for (userDocument in userQuerySnapshot.documents) {
                    val user = userDocument.toObject(User::class.java)
                    if (user != null) {
                        userJoinedList.add(user)
                    }
                }
            }
        }
    }
}