package org.example.project.API

import kotlinx.serialization.Serializable

@Serializable
data class CreateAccountDepartmentResponse(val status: String, val description: String)
