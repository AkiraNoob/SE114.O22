package com.example.collabtask

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CardInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_information)
        setupActionBar()

// Tìm nút (menu)
        val btnPopup : ImageButton = findViewById(R.id.imageButton2menu)
        val btnPopup2 : ImageButton = findViewById(R.id.imageButtonfilter)
// Đặt click listener
        btnPopup2.setOnClickListener{view -> showPopupMenu(view)}
        btnPopup.setOnClickListener{view -> showPopupMenu(view)}

        val btnAttachmentFile : Button = findViewById(R.id.buttonthemtep)
        btnAttachmentFile.setOnClickListener {
            startActivity(Intent(this, fileAttachmentActivity::class.java))
        }

        val btnLabel : View = findViewById(R.id.vthemnhan)
        btnLabel.setOnClickListener {
            startActivity(Intent(this, labelActivity::class.java))
        }
    }

    private fun setupActionBar() {
        val toolBar : androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbarBack)
        setSupportActionBar(toolBar)

        val actionBar = supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.chevron_left_24)
        }

        toolBar.setNavigationOnClickListener{ onBackPressed()}
    }
// Chức năng hiện pop up menu
    private fun showPopupMenu(view: View) {

// Tạo pop-up menu
        val popupMenu = PopupMenu(this, view)
        val popupMenufilter = PopupMenu(this,view)
// Inflate
        popupMenu.menuInflater.inflate(R.menu.delete_menu, popupMenu.menu)
        popupMenufilter.menuInflater.inflate(R.menu.filter_menu,popupMenu.menu)
// Đặt click listener
        popupMenufilter.setOnMenuItemClickListener { menuItem2->handleMenuItemClick(menuItem2)
            true}
        popupMenu.setOnMenuItemClickListener{menuItem->handleMenuItemClick(menuItem)
            true}
// Hiện menu
        popupMenufilter.show()
        popupMenu.show()
    }

// Chức năng click lựa chọn
     private fun handleMenuItemClick(menuItem: MenuItem){
         when(menuItem.itemId){
             R.id.action_delete -> {
                 Toast.makeText(this, "Đã xóa thẻ", Toast.LENGTH_SHORT).show()
             }
             R.id.action_showcomment -> {
                 Toast.makeText(this, "Đã hiện bình luận", Toast.LENGTH_SHORT).show()
             }
             R.id.action_showaction -> {
                 Toast.makeText(this, "Đã hiện hoạt động", Toast.LENGTH_SHORT).show()
             }
             R.id.action_showboth-> {
                 Toast.makeText(this, "Đã hiện cả hai", Toast.LENGTH_SHORT).show()
             }
         }
     }


}