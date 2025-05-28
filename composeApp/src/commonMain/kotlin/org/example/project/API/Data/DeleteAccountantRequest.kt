package org.example.project.API.Data

import org.example.project.Model.AccountsDepartment
import org.example.project.Model.User

data class DeleteAccountantRequest(
    val user: User,
    val accountsDepartment: AccountsDepartment
)
