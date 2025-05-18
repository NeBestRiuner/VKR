package org.example.project.API

import kotlinx.serialization.Serializable

@Serializable

data class DeleteBPTaskResponse(
    val status: String,
    val description: String
)
