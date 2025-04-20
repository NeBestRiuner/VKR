package org.example.project.model

import kotlinx.serialization.Serializable

@Serializable

data class Permission(val id: Int, val name: String, val description: String)
