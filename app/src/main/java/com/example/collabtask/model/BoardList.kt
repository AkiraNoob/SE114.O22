package com.example.collabtask.model

data class BoardList(
    val listId: String,
    val listName: String,
    val cardIds: List<String>,
    val boardId: String
)
