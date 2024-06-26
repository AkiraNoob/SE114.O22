package com.example.collabtask.model

import java.util.Date

data class CardComment(
    val ownerId: String = "",
    val cardId: String = "",
    val content: String = "",
    val commentDate: Date = Date()
)

enum class CardActionType {
    ADD_FILE, REMOVE_FILE, USER_JOIN, USER_LEAVE, ADD_USER, REMOVE_USER, ADD_TO_LIST
}

data class CardAction(
    val cardId: String = "",
    val type: CardActionType = CardActionType.ADD_TO_LIST,
    val ownerId: String = "",
    val affectUserId: String? = "",
    val actionDate: Date = Date(),
    val affectFileName: String? = "",
    val affectBoardListId: String? = ""
)

