package com.example.collabtask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class labelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.label_list)

//        database = FirebaseDatabase.getInstance()
//        itemsRef = database.getReference("boardList")
//
//        // Find views in the layout
//        btnAddItem = findViewById(R.id.ibaddlabel)
//        rvItems = findViewById(R.id.rv_items)
//
//        // Initialize the list and adapter
//        itemsList = mutableListOf()
//        itemsAdapter = ItemAdapter(itemsList)
//        rvItems.layoutManager = LinearLayoutManager(this)
//        rvItems.adapter = itemsAdapter
//
//        // Load existing items from Firebase
//        loadItemsFromFirebase()
//
//        // Set a click listener on the button to add the item
//        btnAddItem.setOnClickListener {
//            val item = etItem.text.toString().trim()
//            if (!TextUtils.isEmpty(item)) {
//                addItemToFirebase(item)
//                etItem.text.clear()
//            } else {
//                Toast.makeText(this, "Please enter an item", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private lateinit var etItem: EditText
//    private lateinit var btnAddItem: Button
//    private lateinit var rvItems: RecyclerView
//    private lateinit var itemsAdapter: ItemAdapter
//    private lateinit var itemsList: MutableList<String>
//    private lateinit var database: FirebaseDatabase
//    private lateinit var itemsRef: DatabaseReference
//
//    private fun loadItemsFromFirebase() {
//        itemsRef.addValueEventListener(object : ValueEventListener {
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onDataChange(snapshot: DataSnapshot) {
//                itemsList.clear()
//                for (itemSnapshot in snapshot.children) {
//                    val item = itemSnapshot.getValue(String::class.java)
//                    if (item != null) {
//                        itemsList.add(item)
//                    }
//                }
//                itemsAdapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@labelActivity, "Failed to load items", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    // Function to add an item to Firebase
//    private fun addItemToFirebase(item: String) {
//        val newItemRef = itemsRef.push()
//        newItemRef.setValue(item).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show()
//            }
//        }
    }
}