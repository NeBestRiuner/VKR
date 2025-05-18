package org.example.project.API

import kotlinx.serialization.Serializable
import org.example.project.model.AccountsDepartment
import org.example.project.model.BPTask

@Serializable

data class DeleteBPTaskRequest(
    val accountsDepartment: AccountsDepartment,
    val bpTask: BPTask
)
