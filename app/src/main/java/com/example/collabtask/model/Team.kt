package com.example.collabtask.model

data class Team(
    val teamId: String,
    val name: String,
    val boardListId: String
)

data class UserJoinedTeam(
    val teamId: String,
    val userId: String
)