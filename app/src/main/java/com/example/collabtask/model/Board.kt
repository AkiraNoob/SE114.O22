package com.example.collabtask.model

data class Board(
    val boardId: String,
    val name: String,
    val teamId: String,
)

data class UserJoinedBoard(
    val boardId: String,
    val userId: String
)