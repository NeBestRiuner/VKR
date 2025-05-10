package org.example.project.API

import kotlinx.serialization.Serializable

@Serializable

data class SendMessageResponse(
    val status: String,
    val description: String
)
