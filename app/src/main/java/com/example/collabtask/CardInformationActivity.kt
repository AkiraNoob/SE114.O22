package com.example.collabtask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class CardInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_information)
        setupActionBar()
    }

    private fun setupActionBar(){
        setSupportActionBar(findViewById(R.id.toolbarBack))

        val actionbar = supportActionBar
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
    }
}