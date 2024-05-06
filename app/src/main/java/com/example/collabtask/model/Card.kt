package com.example.collabtask.model

import java.util.Date

data class Card(
    val taskId: String,
    val taskName: String,
    val description: String? = "",
    val dueDate: Date? = null,
    val boardListId: String,
    val userJoinedCard: List<String>
)
