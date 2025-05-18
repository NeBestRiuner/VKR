package org.example.project.API

import kotlinx.serialization.Serializable

@Serializable

data class DeleteBPResponse(
    val status: String,
    val description: String
)
