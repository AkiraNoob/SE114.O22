package com.example.collabtask.model

import java.util.Date

data class Card(
    var name: String = "",
    var listId: String = "",
    var description: String? = "",
    var dueDate: Date? = null,
    var userJoinedCard: List<String>? = null,
    var labelList: List<Label>? = null,
    var checklistList: List<CardChecklist>? = null,
    var fileList: List<CardFile>? = null,
    var commentList: List<CardComment>? = null,
    var actionList: List<CardAction>? = null
)
