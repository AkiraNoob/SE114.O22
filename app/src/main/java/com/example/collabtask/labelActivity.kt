package com.example.collabtask

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class labelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.label_list)


        val btnAdd : ImageButton = findViewById(R.id.ibaddlabel)
        btnAdd.setOnClickListener {
            startActivity(Intent(this, listInformationActivity::class.java))
        }
    }
}