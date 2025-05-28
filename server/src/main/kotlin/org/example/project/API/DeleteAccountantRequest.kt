package org.example.project.API

import kotlinx.serialization.Serializable
import org.example.project.model.AccountsDepartment

@Serializable

data class DeleteAccountantRequest(
    val user: UserAPI,
    val accountsDepartment: AccountsDepartment
)
