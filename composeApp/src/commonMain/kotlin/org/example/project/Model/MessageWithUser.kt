package org.example.project.Model

data class MessageWithUser(
    val text: String,
    val file: ByteArray,
    val createDate: String,
    val user: String
)
