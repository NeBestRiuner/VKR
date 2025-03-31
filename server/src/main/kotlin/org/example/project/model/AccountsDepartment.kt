package org.example.project.model

import kotlinx.serialization.Serializable
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.util.Date

@Serializable
data class AccountsDepartment(
    val id: Int?,
    val name: String,
    val createDate: String, // строка для хранения времени
    val authorLogin: String
)
