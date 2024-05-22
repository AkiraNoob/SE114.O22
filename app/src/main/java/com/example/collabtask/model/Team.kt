package com.example.collabtask.model

data class Team(
    val teamId: String,
    val name: String,
    val ownerId: String
)

data class UserJoinedTeam(
    val teamId: String,
    val userId: String
)