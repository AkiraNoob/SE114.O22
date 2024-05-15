package com.example.collabtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collabtask.databinding.BoardListDetailFragmentBinding
import com.example.collabtask.model.Card
import com.example.collabtask.model.Label
import com.example.collabtask.model.User

class BoardListDetailFragment : Fragment() {
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
        binding.boardCardsList.adapter = BoardDetailAdapter(itemList)

        for (cardId in 1..5) {

            val labelList: MutableList<Label> = mutableListOf()
            val userJoinedList: MutableList<User> = mutableListOf()

            if (cardId % 2 == 0) {
                labelList.add(Label("1", cardId.toString(), "#00FFFF", "abc"))
                labelList.add(Label("1", cardId.toString(), "#993300", "abc"))
                labelList.add(Label("1", cardId.toString(), "#FFC0CB", "abc"))

                userJoinedList.add(User("3", "nam"))
                userJoinedList.add(User("4", "nam"))
                userJoinedList.add(User("5", "nam"))
            }

            itemList.add(
                Card(
                    cardId.toString(),
                    "Abc",
                    "123",
                    "Lorem ipsum",
                    labelList = labelList
                )
            )
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
            holder.boardCardCommentNumOfContent.text = currentItem.commentList.size.toString()
        }

        if (currentItem.labelList.isNullOrEmpty()) {
            holder.boardCardLabelList.visibility = View.GONE
        }

        if(currentItem.userJoinedCard.isNullOrEmpty()){
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

    private val userJoinedList: MutableList<User> = mutableListOf()
    fun bind(item: Card) {
        // Set up inner RecyclerView
        if (!item.labelList.isNullOrEmpty()) {
            val labelsListRecyclerView: RecyclerView =
                itemView.findViewById(R.id.board_card_labels_list)
            labelsListRecyclerView.setHasFixedSize(true)

            val layoutManager = LinearLayoutManager(itemView.context)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL

            labelsListRecyclerView.layoutManager = layoutManager

            labelsListRecyclerView.adapter = CardLabelItemAdapter(item.labelList)
        }

        if (!item.userJoinedCard.isNullOrEmpty()) {
            val usersListRecyclerView: RecyclerView =
                itemView.findViewById(R.id.board_card_users_list)
            usersListRecyclerView.setHasFixedSize(true)
            usersListRecyclerView.layoutManager = LinearLayoutManager(itemView.context)

            for (userId in item.userJoinedCard) {
                userJoinedList.add(User(userId, "Abc"))
            }

            usersListRecyclerView.adapter = CardUserJoinedItemAdapter(userJoinedList)
        }
    }
}