package org.example.project.Model

import kotlinx.serialization.Serializable

@Serializable
data class User( val login: String, val password: String)
