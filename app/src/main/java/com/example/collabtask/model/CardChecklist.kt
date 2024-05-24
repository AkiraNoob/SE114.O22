package com.example.collabtask.model

data class CardChecklist(
    val content: String,
    val isDone: Boolean = false,
    val cardId: String
)