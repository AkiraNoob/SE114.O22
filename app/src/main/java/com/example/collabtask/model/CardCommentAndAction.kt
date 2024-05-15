package com.example.collabtask.model

import java.util.Date

data class CardComment(
    val ownerId: String,
    val content: String,
    val commentDate: Date
)

enum class CardActionType {
    ADD_FILE, REMOVE_FILE, USER_JOIN, USER_LEAVE, ADD_USER, REMOVE_USER, ADD_TO_LIST
}

data class CardAction(
    val type: CardActionType,
    val ownerId: String,
    val affectUserId: String?,
    val actionDate: Date,
    val affectFileName: String?,

    )

