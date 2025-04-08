package org.example.project.API

import kotlinx.serialization.Serializable

@Serializable

data class AccountsDepartmentAPI(
    val name: String,
    val createDate: String, // строка для хранения времени
    val authorLogin: String
)
