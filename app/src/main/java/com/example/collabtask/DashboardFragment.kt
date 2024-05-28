package com.example.collabtask

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collabtask.databinding.DashboardFragmentBinding
import com.example.collabtask.use_case.DashboardApiUseCases
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {
    private var _binding: DashboardFragmentBinding? = null

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

        viewLifecycleOwner.lifecycleScope.launch {
            val itemList = DashboardApiUseCases.getUserJoinedTeam()
            if (itemList != null) {
                binding.dashboardGroupList.adapter =
                    DashboardGroupListAdapter(itemList, findNavController())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class DashboardGroupListAdapter(
    private val itemList: List<DocumentSnapshot>,
    private val navController: NavController
) :
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
        holder.textGroupName.text = currentItem.get("name").toString()
        holder.bind(currentItem, navController)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

class DashboardGroupListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textGroupName: TextView = itemView.findViewById(R.id.dashboard_group_item_title_name)
    val textGroupNavigate: TextView =
        itemView.findViewById(R.id.dashboard_group_item_title_navigate)

    fun bind(item: DocumentSnapshot, navController: NavController) { //item is Team
        // Set up inner RecyclerView
        val innerRecyclerView: RecyclerView =
            itemView.findViewById(R.id.dashboard_group_item_boards_list)
        innerRecyclerView.setHasFixedSize(true)
        innerRecyclerView.layoutManager = LinearLayoutManager(itemView.context)

        CoroutineScope(Dispatchers.Main).launch {
            val boardItemList = DashboardApiUseCases.getRecentBoardsOfTeam(item.id as String)
            innerRecyclerView.adapter =
                DashboardGroupItemAdapter(item.id, boardItemList, navController)
        }
    }
}