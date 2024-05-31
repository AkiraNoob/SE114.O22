package com.example.collabtask

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CardInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_information)
        setupActionBar()


        val btnBrowseImage: TextView = findViewById(R.id.tvchontuthuvienanh)
        btnBrowseImage.setOnClickListener {
            openImagePicker()
        }
// Tìm nút (menu)
        val btnPopup : ImageButton = findViewById(R.id.imageButton2menu)
        val btnPopup2 : ImageButton = findViewById(R.id.imageButtonfilter)

// Đặt click listener (menu)
        btnPopup2.setOnClickListener{view -> showPopupFilterMenu(view)}
        btnPopup.setOnClickListener{view -> showPopupMenu(view)}

        // Đặt click listener (layout)
        val overlayLayout: View = findViewById(R.id.rloverlayattachment)
        val btnAttachmentFile : Button = findViewById(R.id.buttonthemtep)

        btnAttachmentFile.setOnClickListener {
            if(overlayLayout.visibility == View.GONE){
                overlayLayout.visibility = View.VISIBLE
            }
            else overlayLayout.visibility = View.GONE
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
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.delete_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener{menuItem->handleMenuItemClick(menuItem)
            true}
        popupMenu.show()
    }

    private fun showPopupFilterMenu(view: View) {
        val popupMenufilter = PopupMenu(this,view)
        popupMenufilter.menuInflater.inflate(R.menu.filter_menu, popupMenufilter.menu)
        popupMenufilter.setOnMenuItemClickListener { menuItem2->handleFilterMenuItemClick(menuItem2)
            true}
        popupMenufilter.show()
    }

// Chức năng click lựa chọn
     private fun handleMenuItemClick(menuItem: MenuItem){
         when(menuItem.itemId){
             R.id.action_delete -> {
                 Toast.makeText(this, "Đã xóa thẻ", Toast.LENGTH_SHORT).show()
             }
         }
     }
    private fun handleFilterMenuItemClick(menuItem2: MenuItem){
        when(menuItem2.itemId){
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

    private val PICK_IMAGE_REQUEST = 1
    private fun openImagePicker(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
}