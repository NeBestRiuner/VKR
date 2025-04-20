package org.example.project.API

import kotlinx.serialization.Serializable

@Serializable
data class  LoginUserResponse(
    val status: String,
    val description: String,
    val authToken: String
)
