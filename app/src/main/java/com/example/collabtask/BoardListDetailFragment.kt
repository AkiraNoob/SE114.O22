package com.example.collabtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collabtask.databinding.BoardListDetailFragmentBinding
import com.example.collabtask.model.Card
import com.example.collabtask.model.User
import com.example.collabtask.use_case.BoardListDetailApiUseCases
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class BoardListDetailFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private var _binding: BoardListDetailFragmentBinding? = null
    private val itemList: MutableList<Card> = mutableListOf()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BoardListDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.boardCardsList.layoutManager = LinearLayoutManager(context)


        binding.menuBtn.setOnClickListener {
            val popup = PopupMenu(context, it).apply {
                setOnMenuItemClickListener(this@BoardListDetailFragment)
            }
            popup.menuInflater.inflate(R.menu.add_card_menu, popup.menu)
            popup.setForceShowIcon(true)
            popup.show()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val cardList = BoardListDetailApiUseCases.getCardsOfBoardList("Fh77v9HiodxqbkBjawfD")

            binding.boardCardsList.adapter = BoardDetailAdapter(cardList)
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_card -> {
                val dialog = AddCardDialogFragment("Fh77v9HiodxqbkBjawfD")
                dialog.show(requireFragmentManager(), "add_card_dialog")
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class BoardDetailAdapter(private val itemList: List<Card>) :
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

    fun bind(item: Card) {
        // Set up inner RecyclerView
        if (!item.labelList.isNullOrEmpty()) {
            val labelsListRecyclerView: RecyclerView =
                itemView.findViewById(R.id.board_card_labels_list)
            labelsListRecyclerView.setHasFixedSize(true)

            val layoutManager = LinearLayoutManager(itemView.context)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL

            labelsListRecyclerView.layoutManager = layoutManager

            labelsListRecyclerView.adapter = CardLabelItemAdapter(item.labelList!!)
        }

        if (!item.userJoinedCard.isNullOrEmpty()) {
            val userJoinedList: MutableList<User> = mutableListOf()

            val usersListRecyclerView: RecyclerView =
                itemView.findViewById(R.id.board_card_users_list)
            usersListRecyclerView.setHasFixedSize(true)
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