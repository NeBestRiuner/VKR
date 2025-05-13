package org.example.project.API

import kotlinx.serialization.Serializable

@Serializable

data class UpdateBusinessProcessResponse(
    val status: String,
    val description: String
)
