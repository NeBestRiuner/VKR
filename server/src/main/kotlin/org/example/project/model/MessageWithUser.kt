package org.example.project.model

import kotlinx.serialization.Serializable

@Serializable

data class MessageWithUser(
    val text: String,
    val file: ByteArray,
    val createDate: String,
    val user: String
)
