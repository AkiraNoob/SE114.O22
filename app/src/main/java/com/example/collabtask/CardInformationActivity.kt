package com.example.collabtask

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CardInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_information)
        setupActionBar()

        tvSelectedDateTime = findViewById(R.id.tvngayhethan)

        val btnBrowseImage: TextView = findViewById(R.id.tvchontuthuvienanh)
        imgSelected = findViewById(R.id.ivanh)

        btnBrowseImage.setOnClickListener {
            openImagePicker()
        }
// Tìm nút (menu)
        val btnPopup: ImageButton = findViewById(R.id.imageButton2menu)
        val btnPopup2: ImageButton = findViewById(R.id.imageButtonfilter)

// Đặt click listener (menu)
        btnPopup2.setOnClickListener { view -> showPopupFilterMenu(view) }
        btnPopup.setOnClickListener { view -> showPopupMenu(view) }

        // Đặt click listener (layout)
        val overlayLayout: View = findViewById(R.id.rloverlayattachment)
        val btnAttachmentFile: Button = findViewById(R.id.buttonthemtep)

        btnAttachmentFile.setOnClickListener {
            if (overlayLayout.visibility == View.GONE) {
                overlayLayout.visibility = View.VISIBLE
            } else overlayLayout.visibility = View.GONE
        }

        val btnLabel: View = findViewById(R.id.vthemnhan)
        btnLabel.setOnClickListener {
            startActivity(Intent(this, labelActivity::class.java))
        }

        val btnPickDateTime: View = findViewById(R.id.vngayhethan)

        btnPickDateTime.setOnClickListener {
            showDatePicker()
        }
    }

    private fun setupActionBar() {
        val toolBar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbarBack)
        setSupportActionBar(toolBar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.chevron_left_24)
        }
        toolBar.setNavigationOnClickListener { onBackPressed() }
    }

    // Chức năng hiện pop up menu
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.delete_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            handleMenuItemClick(menuItem)
            true
        }
        popupMenu.show()
    }

    private fun showPopupFilterMenu(view: View) {
        val popupMenufilter = PopupMenu(this, view)
        popupMenufilter.menuInflater.inflate(R.menu.filter_menu, popupMenufilter.menu)
        popupMenufilter.setOnMenuItemClickListener { menuItem2 ->
            handleFilterMenuItemClick(menuItem2)
            true
        }
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

    private lateinit var imgSelected: ImageView
    private val PICK_IMAGE_REQUEST = 1

    private lateinit var tvSelectedDateTime: TextView
    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedDay = 0
    private var selectedHour = 0
    private var selectedMinute = 0

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            selectedImageUri?.let {
                imgSelected.setImageURI(it)
            }
        }
    }
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            this.selectedYear = selectedYear
            this.selectedMonth = selectedMonth
            this.selectedDay = selectedDay
            showTimePicker()
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            this.selectedHour = selectedHour
            this.selectedMinute = selectedMinute
            displaySelectedDateTime()
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun displaySelectedDateTime() {
        val selectedDateTime = "$selectedDay/${selectedMonth + 1}/$selectedYear $selectedHour:$selectedMinute"
        tvSelectedDateTime.text = selectedDateTime
    }
}