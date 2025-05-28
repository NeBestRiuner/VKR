package org.example.project.API

import kotlinx.serialization.Serializable

@Serializable

data class DeleteAccountantResponse(
    val status: String,
    val description: String
)
