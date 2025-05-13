package org.example.project.API

import kotlinx.serialization.Serializable

@Serializable

data class RunBusinessProcessResponse(
    val status: String,
    val description: String
)
