package com.example.collabtask

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class listInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_information)

        val backButton1 : ImageButton = findViewById(R.id.ibBacktoCard)
        backButton1.setOnClickListener{
            startActivity(Intent(this, CardInformationActivity::class.java))
        }

        val backButton2 : ImageButton = findViewById(R.id.ibbackbutton)
        backButton2.setOnClickListener{
            startActivity(Intent(this, labelActivity::class.java))
        }
    }
}