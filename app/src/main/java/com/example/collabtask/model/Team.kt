package com.example.collabtask.model

data class Team(
    val name: String = "",
    val ownerId: String = ""
)

data class UserJoinedTeam(
    val teamId: String = "",
    val userId: String = ""
)