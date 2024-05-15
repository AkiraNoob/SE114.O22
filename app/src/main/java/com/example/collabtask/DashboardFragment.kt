package com.example.collabtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collabtask.databinding.DashboardFragmentBinding
import com.example.collabtask.model.Board
import com.example.collabtask.model.Team

class DashboardFragment : Fragment() {
    private var _binding: DashboardFragmentBinding? = null
    private val itemList: MutableList<Team> = mutableListOf()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DashboardFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dashboardGroupList.layoutManager = LinearLayoutManager(context)

        itemList.add(Team("1", "Nhóm 1", "1"))
        itemList.add(Team("2", "Nhóm 2", "1"))
        itemList.add(Team("3", "Nhóm 3", "1"))
        itemList.add(Team("4", "Nhóm 4", "1"))

        binding.dashboardGroupList.adapter = DashboardGroupListAdapter(itemList, findNavController())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class DashboardGroupListAdapter(private val itemList: List<Team>, private val navController: NavController) :
    RecyclerView.Adapter<DashboardGroupListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DashboardGroupListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_group_item_fragment, parent, false)
        return DashboardGroupListViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardGroupListViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.textGroupName.text = currentItem.name
        holder.bind(itemList[position], navController)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

class DashboardGroupListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textGroupName: TextView = itemView.findViewById(R.id.dashboard_group_item_title_name)
    val textGroupNavigate: TextView =
        itemView.findViewById(R.id.dashboard_group_item_title_navigate)
    private val boardItemList: MutableList<Board> = mutableListOf()

    fun bind(item: Team, navController: NavController) {
        // Set up inner RecyclerView
        val innerRecyclerView: RecyclerView =
            itemView.findViewById(R.id.dashboard_group_item_boards_list)
        innerRecyclerView.setHasFixedSize(true)
        innerRecyclerView.layoutManager = LinearLayoutManager(itemView.context)

        for (boardId in 1..3) {
            boardItemList.add(Board(boardId.toString(), "Bảng $boardId", "1"))
        }

        innerRecyclerView.adapter = DashboardGroupItemAdapter(item.teamId, boardItemList, navController)
    }
}