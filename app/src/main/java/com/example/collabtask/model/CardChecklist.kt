package com.example.collabtask.model

data class CardChecklist(
    val checklistId: String,
    val content: String,
    val isDone: Boolean = false
)