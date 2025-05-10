package org.example.project.API.Data

import org.example.project.Model.MessageWithUser

data class GetMessageListResponse(
    val status: String,
    val description: String,
    val messageWUList: List<MessageWithUser>
)
