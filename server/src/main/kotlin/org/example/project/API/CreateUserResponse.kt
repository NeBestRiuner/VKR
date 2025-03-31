package org.example.project.API

import kotlinx.serialization.Serializable
import org.example.project.model.User

@Serializable
data class CreateUserResponse(
    val status: String,
    val user: UserAPI?
)