package org.example.project.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int?,
    val login: String,
    val password: String
)
