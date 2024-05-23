package com.example.collabtask.model

import java.util.Date

data class Card(
    val name: String,
    val listId: String,
    val description: String? = "",
    val dueDate: Date? = null,
    val userJoinedCard: List<String>? = null,
    val labelList: List<Label>? = null,
    val checklistList: List<CardChecklist>? = null,
    val fileList: List<CardFile>? = null,
    val commentList: List<CardComment>? = null,
    val actionList: List<CardAction>? = null
)
