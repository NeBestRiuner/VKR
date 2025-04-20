package org.example.project.API.Data

data class LoginUserResponse(
    val status: String,
    val description: String,
    val authToken: String
)
