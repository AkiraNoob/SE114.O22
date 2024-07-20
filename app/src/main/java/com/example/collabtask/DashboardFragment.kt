package com.example.collabtask

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
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

class DashboardFragment : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    private lateinit var binding: DashboardFragmentBinding

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DashboardFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.dashboardGroupList.layoutManager = LinearLayoutManager(applicationContext)

        lifecycleScope.launch {
            val itemList = DashboardApiUseCases.getUserJoinedTeam()
            if (itemList != null) {
                binding.dashboardGroupList.adapter =
                    DashboardGroupListAdapter(
                        itemList,
                        { navigateToBoardDetail(it) },
                        { navigateToGroupDetail(it) })
            }
            return@launch
        }

        binding.addBtn.setOnClickListener {
            val popup = PopupMenu(applicationContext, it).apply {
                setOnMenuItemClickListener(this@DashboardFragment)
            }
            popup.menuInflater.inflate(R.menu.dashboard_add_menu, popup.menu)
            popup.setForceShowIcon(true)
            popup.show()
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_team -> {
                val dialog = AddTeamDialogFragment()
                dialog.show(supportFragmentManager, "add_team_dialog")
                true
            }
//            R.id.add_personal_board -> {
//                val dialog = AddPersonalBoardDialogFragment()
//                dialog.show(requireFragmentManager(), "add_personal_board_dialog")
//                true
//            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun navigateToBoardDetail(boardId: String) {
        val intent = Intent(this, BoardDetail::class.java)
        intent.putExtra("boardId", boardId)
        startActivity(intent)
    }

    private fun navigateToGroupDetail(groupDetail: String) {
        val intent = Intent(this, GroupDetailActivity::class.java)
        intent.putExtra("groupId", groupDetail)
        startActivity(intent)
    }
}

class DashboardGroupListAdapter(
    private val itemList: List<DocumentSnapshot>,
    private val navigateToBoardDetail: (String) -> Unit,
    private val navigateToGroupDetail: (String) -> Unit
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
        holder.bind(currentItem, navigateToBoardDetail)
        holder.textGroupNavigate.setOnClickListener {
            navigateToGroupDetail(currentItem.id)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

class DashboardGroupListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textGroupName: TextView = itemView.findViewById(R.id.dashboard_group_item_title_name)
    val textGroupNavigate: TextView =
        itemView.findViewById(R.id.dashboard_group_item_title_navigate)

    fun bind(item: DocumentSnapshot, navigateToBoardDetail: (String) -> Unit) { //item is Team
        // Set up inner RecyclerView
        val innerRecyclerView: RecyclerView =
            itemView.findViewById(R.id.dashboard_group_item_boards_list)
        innerRecyclerView.layoutManager = LinearLayoutManager(itemView.context)

        CoroutineScope(Dispatchers.Main).launch {
            val boardItemList = DashboardApiUseCases.getRecentBoardsOfTeam(item.id)
            innerRecyclerView.adapter =
                DashboardGroupItemAdapter(item.id, boardItemList, navigateToBoardDetail)
            return@launch
        }
    }
}