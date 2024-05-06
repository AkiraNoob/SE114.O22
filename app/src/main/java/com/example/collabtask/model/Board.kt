package com.example.collabtask.model

data class Board(
    val boardId: String,
    val name: String,
    val cover: String? = "",
    val teamId: String
)

