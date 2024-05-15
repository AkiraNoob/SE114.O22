package com.example.collabtask.model

enum class FileType {
    IMAGE, FILE
}

data class CardFile(
    val type: FileType,
    val name: String,
    val fileId: String,
)
