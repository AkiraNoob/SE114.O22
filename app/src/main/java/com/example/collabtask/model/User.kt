package com.example.collabtask.model

import java.lang.reflect.Constructor
import java.util.UUID

data class User(
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    val avatar: String? = "",
)

