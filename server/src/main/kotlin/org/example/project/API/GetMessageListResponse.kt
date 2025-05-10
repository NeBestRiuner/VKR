package org.example.project.API


import kotlinx.serialization.Serializable
import org.example.project.model.MessageWithUser

@Serializable

data class GetMessageListResponse(
    val status: String,
    val description: String,
    val messageWUList: List<MessageWithUser>
)
