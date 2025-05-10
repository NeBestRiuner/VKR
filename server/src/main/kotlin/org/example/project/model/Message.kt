package org.example.project.model

import kotlinx.serialization.Serializable

@Serializable

data class Message(
    val text: String,
    val file: ByteArray,
    val createDate: String
)
