package org.example.project.API

import kotlinx.serialization.Serializable

@Serializable

data class DeleteTaskResponse(
    val status: String,
    val description: String
)
