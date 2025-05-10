package org.example.project.API

import kotlinx.serialization.Serializable

@Serializable

data class UpdateTaskResponse(
    val status: String,
    val description: String
)
